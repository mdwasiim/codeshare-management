    package com.codeshare.airline.auth.security;


    import com.codeshare.airline.auth.entities.authorization.PermissionRole;
    import com.codeshare.airline.auth.entities.identity.Permission;
    import com.codeshare.airline.auth.entities.identity.User;
    import com.codeshare.airline.auth.service.RolePermissionResolverService;
    import com.codeshare.airline.auth.utils.mappers.PermissionMapper;
    import com.codeshare.airline.auth.utils.mappers.RoleMapper;
    import com.codeshare.airline.auth.utils.mappers.UserMapper;
    import com.codeshare.airline.common.auth.model.PermissionDTO;
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;
    import jakarta.annotation.PostConstruct;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    import javax.crypto.SecretKey;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Set;
    import java.util.stream.Collectors;

    @Component
    public class JwtUtil {

        @Value("${jwt.secret}")
        private String secretString;

        @Value("${jwt.access-expiration}")
        private long accessExpiration;

        @Value("${jwt.refresh-expiration}")
        private long refreshExpiration;

        private SecretKey secretKey;

        private final RoleMapper roleMapper;
        private final PermissionMapper permissionMapper;
        private final UserMapper userMapper;
        private final RolePermissionResolverService rolePermissionResolverService;

        @Autowired
        public JwtUtil(RoleMapper roleMapper,
                       PermissionMapper permissionMapper,
                       UserMapper userMapper,
                       RolePermissionResolverService rolePermissionResolverService) {
            this.roleMapper = roleMapper;
            this.permissionMapper = permissionMapper;
            this.userMapper = userMapper;
            this.rolePermissionResolverService = rolePermissionResolverService;
        }

        @PostConstruct
        public void init() {
            // Convert string secret to a proper SecretKey (HS512 needs 64 bytes minimum)
            byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretString);
            secretKey = Keys.hmacShaKeyFor(keyBytes);
        }

        public String generateAccessToken(User user) {
            Map<String, Object> claims = new HashMap<>();

            claims.put("roles", rolePermissionResolverService
                    .resolveRoleNames(user.getId(), user.getTenantId()));

            claims.put("permissions", rolePermissionResolverService
                    .resolvePermissionsNames(user.getId(), user.getTenantId()));

            claims.put("tenantId", user.getTenantId());
            claims.put("organizationId", user.getOrganizationId());

            return buildToken(claims, user.getUsername(), accessExpiration);
        }

        public String generateRefreshToken(User user) {
            return buildToken(new HashMap<>(), user.getUsername(), refreshExpiration);
        }

        private String buildToken(Map<String, Object> claims, String subject, long expiration) {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();
        }

        public Claims extractClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        public boolean validateToken(String token, String username) {
            Claims claims = extractClaims(token);

            return claims.getSubject().equals(username)
                    && claims.getExpiration().after(new Date());
        }
        public Set<PermissionDTO> getAllUserPermissions(User user) {
            Set<Permission> directPermissions = user.getUserRoles().stream()
                    .flatMap(ur -> ur.getRole().getPermissionRoles().stream())
                    .map(PermissionRole::getPermission)
                    .collect(Collectors.toSet());

            Set<Permission> groupPermissions = user.getUserGroupRoles().stream()
                    .flatMap(ugr -> ugr.getRole().getPermissionRoles().stream())
                    .map(PermissionRole::getPermission)
                    .collect(Collectors.toSet());

            directPermissions.addAll(groupPermissions); // merge

            return permissionMapper.toDTOSet(directPermissions);
        }
    }
