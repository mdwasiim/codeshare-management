package com.codeshare.airline.tools.ssim.master.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public record ImportConfig(
        String dbUrl,
        String dbUser,
        String dbPassword,
        String schema,
        String auditUser,
        String ssimFile,
        boolean dryRun
) {
    public static ImportConfig from(String[] args) throws IOException {
        Map<String, String> cli = parseArgs(args);
        Properties properties = new Properties();

        loadProperties(cli, properties);

        String dryRun = cli.getOrDefault("dry-run", value(cli, properties, "import.dry-run", "true"));

        return new ImportConfig(
                value(cli, properties, "db.url", null),
                value(cli, properties, "db.user", null),
                value(cli, properties, "db.password", null),
                value(cli, properties, "import.schema", "schedule_master_data"),
                value(cli, properties, "import.audit-user", "SSIM_IMPORTER"),
                value(cli, properties, "ssim.file", null),
                Boolean.parseBoolean(dryRun)
        );
    }

    private static String value(Map<String, String> cli, Properties properties, String key, String defaultValue) {
        String cliKey = key.replace('.', '-');
        return cli.getOrDefault(cliKey, properties.getProperty(key, defaultValue));
    }

    private static void loadProperties(Map<String, String> cli, Properties properties) throws IOException {
        if (cli.containsKey("config")) {
            Path configured = resolveConfigPath(cli.get("config"));
            loadFile(properties, configured);
            return;
        }

        Path workingDirConfig = Path.of("application.properties").toAbsolutePath().normalize();
        if (Files.isRegularFile(workingDirConfig)) {
            loadFile(properties, workingDirConfig);
            return;
        }

        Path jarDirConfig = jarDirectory().resolve("application.properties").normalize();
        if (Files.isRegularFile(jarDirConfig)) {
            loadFile(properties, jarDirConfig);
        }
    }

    private static Path resolveConfigPath(String configPath) throws IOException {
        Path configured = Path.of(configPath).toAbsolutePath().normalize();
        if (Files.isRegularFile(configured)) {
            return configured;
        }

        Path resourcesConfig = Path.of("src", "main", "resources", configPath).toAbsolutePath().normalize();
        if (Files.isRegularFile(resourcesConfig)) {
            return resourcesConfig;
        }

        Path jarDirConfig = jarDirectory().resolve(configPath).normalize();
        if (Files.isRegularFile(jarDirConfig)) {
            return jarDirConfig;
        }

        if ("application.properties".equals(configPath)) {
            Path resourcesExample = Path.of("src", "main", "resources", "application.properties.example")
                    .toAbsolutePath()
                    .normalize();
            if (Files.isRegularFile(resourcesExample)) {
                return resourcesExample;
            }

            Path classpathExample = jarDirectory().resolve("application.properties.example").normalize();
            if (Files.isRegularFile(classpathExample)) {
                return classpathExample;
            }

            throw new IOException("""
                    Config file not found.
                    Searched:
                      %s
                      %s
                      %s
                      %s
                      %s
                    """.formatted(configured, resourcesConfig, jarDirConfig, resourcesExample, classpathExample));
        }

        throw new IOException("""
                Config file not found.
                Searched:
                  %s
                  %s
                  %s
                """.formatted(configured, resourcesConfig, jarDirConfig));
    }

    private static void loadFile(Properties properties, Path path) throws IOException {
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        }
        System.out.println("Loaded config: " + path);
    }

    private static Path jarDirectory() {
        try {
            CodeSource codeSource = ImportConfig.class.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return Path.of(".").toAbsolutePath().normalize();
            }
            Path location = Path.of(codeSource.getLocation().toURI()).toAbsolutePath().normalize();
            return Files.isRegularFile(location) ? location.getParent() : location;
        } catch (Exception ignored) {
            return Path.of(".").toAbsolutePath().normalize();
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> values = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!arg.startsWith("--")) {
                continue;
            }
            String key = arg.substring(2);
            String value = "true";
            int equals = key.indexOf('=');
            if (equals >= 0) {
                value = key.substring(equals + 1);
                key = key.substring(0, equals);
            } else if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                value = args[++i];
            }
            values.put(key, value);
        }
        return values;
    }
}
