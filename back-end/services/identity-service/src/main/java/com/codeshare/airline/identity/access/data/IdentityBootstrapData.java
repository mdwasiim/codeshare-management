package com.codeshare.airline.identity.access.data;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.core.enums.common.TenantPlan;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IdentityBootstrapData {

    private static final String LOCATION = "classpath*:bootstrap/identity/*.json";

    private final ObjectMapper objectMapper;
    private final ResourcePatternResolver resourcePatternResolver;

    private Map<String, List<JsonNode>> sections;

    public List<TenantSeed> tenants() {
        return nodes("tenants").stream()
                .map(node -> new TenantSeed(
                        text(node, "code"),
                        text(node, "name"),
                        text(node, "description"),
                        text(node, "contactEmail"),
                        text(node, "contactPhone"),
                        text(node, "region"),
                        text(node, "logoUrl"),
                        enumValue(TenantPlan.class, node, "plan", TenantPlan.PRO),
                        bool(node, "trial", false)))
                .toList();
    }

    public List<RoleSeed> roles() {
        return nodes("roles").stream()
                .map(node -> new RoleSeed(
                        text(node, "code"),
                        text(node, "name"),
                        text(node, "description")))
                .toList();
    }

    public List<GroupSeed> groups() {
        return nodes("groups").stream()
                .map(node -> new GroupSeed(
                        text(node, "code"),
                        text(node, "name"),
                        text(node, "description")))
                .toList();
    }

    public List<UserSeed> users() {
        return nodes("users").stream()
                .map(node -> new UserSeed(
                        text(node, "username"),
                        text(node, "password"),
                        text(node, "email"),
                        text(node, "firstName"),
                        text(node, "lastName"),
                        enumValue(AuthSource.class, node, "authSource", AuthSource.INTERNAL),
                        bool(node, "enabled", true)))
                .toList();
    }

    public List<PermissionSeed> permissions() {
        return nodes("permissions").stream()
                .map(node -> {
                    String domain = text(node, "domain");
                    String action = text(node, "action");
                    return new PermissionSeed(
                            text(node, "code", (domain + ":" + action).toLowerCase()),
                            domain.toUpperCase(),
                            action.toUpperCase(),
                            text(node, "name", capitalize(action) + " " + capitalize(domain)),
                            text(node, "description", "Allows " + action + " access on " + domain));
                })
                .toList();
    }

    public List<IdentityProviderSeed> identityProviders() {
        return nodes("identity-providers").stream()
                .map(node -> new IdentityProviderSeed(
                        enumValue(AuthSource.class, node, "authSource", AuthSource.INTERNAL),
                        integer(node, "priority", 1),
                        bool(node, "enabled", true),
                        oidcConfig(node.path("oidc"))))
                .toList();
    }

    public Map<String, List<String>> groupRoles() {
        return mapping("group-roles", "group", "roles");
    }

    public Map<String, List<String>> rolePermissions() {
        return mapping("role-permissions", "role", "permissions");
    }

    public Map<String, List<String>> userGroups() {
        return mapping("user-groups", "username", "groups");
    }

    public Map<String, List<String>> groupMenus() {
        return mapping("group-menus", "group", "menus");
    }

    private OidcConfigSeed oidcConfig(JsonNode node) {
        if (!node.isObject()) {
            return null;
        }

        return new OidcConfigSeed(
                text(node, "issuerUri"),
                text(node, "authorizationUri"),
                text(node, "tokenUri"),
                text(node, "jwkSetUri"),
                text(node, "clientId"),
                text(node, "clientSecretRef"),
                text(node, "redirectUri"),
                text(node, "grantType"),
                text(node, "clientAuthMethod"),
                text(node, "scopes"),
                bool(node, "enforceRedirectUri", true));
    }

    private Map<String, List<String>> mapping(String type, String keyField, String valuesField) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (JsonNode node : nodes(type)) {
            result.put(text(node, keyField), strings(node, valuesField));
        }
        return result;
    }

    private List<JsonNode> nodes(String type) {
        ensureLoaded();
        return sections.getOrDefault(type, List.of());
    }

    private synchronized void ensureLoaded() {
        if (sections != null) {
            return;
        }

        try {
            sections = new HashMap<>();
            Resource[] resources = resourcePatternResolver.getResources(LOCATION);
            List<Resource> orderedResources = new ArrayList<>(List.of(resources));
            orderedResources.sort(Comparator.comparing(Resource::getFilename, Comparator.nullsLast(String::compareTo)));

            for (Resource resource : orderedResources) {
                JsonNode root = objectMapper.readTree(resource.getInputStream());
                JsonNode groupedSections = root.path("sections");
                if (groupedSections.isArray()) {
                    for (JsonNode section : groupedSections) {
                        loadSection(section);
                    }
                } else {
                    loadSection(root);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load identity bootstrap JSON resources", ex);
        }
    }

    private void loadSection(JsonNode section) {
        String type = text(section, "type");
        JsonNode items = section.path("items");
        if (type == null || !items.isArray()) {
            return;
        }

        List<JsonNode> target = sections.computeIfAbsent(type, ignored -> new ArrayList<>());
        for (JsonNode item : items) {
            target.add(item);
        }
    }

    private List<String> strings(JsonNode node, String field) {
        JsonNode values = node.path(field);
        if (!values.isArray()) {
            return List.of();
        }

        List<String> result = new ArrayList<>();
        for (JsonNode value : values) {
            result.add(value.asText());
        }
        return result;
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asText();
    }

    private String text(JsonNode node, String field, String fallback) {
        String value = text(node, field);
        return value == null || value.isBlank() ? fallback : value;
    }

    private int integer(JsonNode node, String field, int fallback) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? fallback : value.asInt();
    }

    private boolean bool(JsonNode node, String field, boolean fallback) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? fallback : value.asBoolean();
    }

    private <E extends Enum<E>> E enumValue(Class<E> type, JsonNode node, String field, E fallback) {
        String value = text(node, field);
        return value == null || value.isBlank() ? fallback : Enum.valueOf(type, value);
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    public record TenantSeed(String code,
                             String name,
                             String description,
                             String contactEmail,
                             String contactPhone,
                             String region,
                             String logoUrl,
                             TenantPlan plan,
                             boolean trial) {
    }

    public record RoleSeed(String code, String name, String description) {
    }

    public record GroupSeed(String code, String name, String description) {
    }

    public record UserSeed(String username,
                           String password,
                           String email,
                           String firstName,
                           String lastName,
                           AuthSource authSource,
                           boolean enabled) {
    }

    public record PermissionSeed(String code,
                                 String domain,
                                 String action,
                                 String name,
                                 String description) {
    }

    public record IdentityProviderSeed(AuthSource authSource,
                                       int priority,
                                       boolean enabled,
                                       OidcConfigSeed oidc) {
    }

    public record OidcConfigSeed(String issuerUri,
                                 String authorizationUri,
                                 String tokenUri,
                                 String jwkSetUri,
                                 String clientId,
                                 String clientSecretRef,
                                 String redirectUri,
                                 String grantType,
                                 String clientAuthMethod,
                                 String scopes,
                                 boolean enforceRedirectUri) {
    }
}
