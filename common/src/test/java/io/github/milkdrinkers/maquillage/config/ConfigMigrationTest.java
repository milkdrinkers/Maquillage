package io.github.milkdrinkers.maquillage.config;

import io.github.milkdrinkers.maquillage.config.loading.ConfigLoader;
import io.github.milkdrinkers.maquillage.config.typeserializer.StringListSerializer;
import io.github.milkdrinkers.maquillage.config.typeserializer.StringObjectMapSerializer;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseType;
import io.github.milkdrinkers.maquillage.messaging.broker.BrokerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Reads the config files a Crate era install has on disk and checks the values survive the move to
 * Configurate. The fixtures are the shipped 1.1.2 defaults with a few values changed away from
 * their defaults, so a migration that silently drops a key fails here rather than resetting a
 * server's settings on upgrade.
 */
@DisplayName("Crate era config migrations")
class ConfigMigrationTest {
    private static <T extends VersionedConfig> T migrate(Path dir, String name, String contents, Class<T> type) throws IOException {
        final Path file = dir.resolve(name);
        Files.writeString(file, contents);
        return new ConfigLoader()
            .withPath(file)
            .withHeader("")
            .withSerializer(b -> b
                .registerExact(StringListSerializer.TYPE_TOKEN, StringListSerializer.INSTANCE)
                .registerExact(StringObjectMapSerializer.TYPE_TOKEN, StringObjectMapSerializer.INSTANCE))
            .buildOrThrow(type);
    }

    @Test
    @DisplayName("config.yml: update-checker.enable becomes enabled, module settings are preserved")
    void pluginConfig(@TempDir Path dir) throws IOException {
        final PluginConfig cfg = migrate(dir, "config.yml", """
            update-checker:
              enable: false
              console: true
              op: false
            
            language: "sv_SE"
            
            module:
              tag:
                enabled: false
              namecolor:
                enabled: true
              nickname:
                enabled: true
                prefix:
                  enabled: false
                  string: "~"
                set-displayname: false
                set-listname: true
                length: 32
            """, PluginConfig.class);

        assertFalse(cfg.updateChecker.enabled, "a disabled update checker must not flip back on");
        assertTrue(cfg.updateChecker.console);
        assertFalse(cfg.updateChecker.op);

        assertEquals("sv_SE", cfg.language);

        assertFalse(cfg.module.tag.enabled);
        assertTrue(cfg.module.namecolor.enabled);
        assertTrue(cfg.module.nickname.enabled);
        assertFalse(cfg.module.nickname.prefix.enabled);
        assertEquals("~", cfg.module.nickname.prefix.string);
        assertFalse(cfg.module.nickname.setDisplayname);
        assertTrue(cfg.module.nickname.setListname);
        assertEquals(32, cfg.module.nickname.length);

        final String after = Files.readString(dir.resolve("config.yml"));
        assertTrue(after.contains("enabled: false"));
        assertFalse(after.contains("enable: false"), "the old key must be gone, not shadowed");
    }

    @Test
    @DisplayName("database.yml: pool keys lose their hyphens, address becomes a list, sql becomes database")
    void databaseConfig(@TempDir Path dir) throws IOException {
        final DatabaseConfig cfg = migrate(dir, "database.yml", """
            database:
              type: "mariadb"
              table-prefix: "cosmetics_"
            
              host: "db.example.test"
              port: 3307
              database: "maquillage"
              username: "mq"
              password: "hunter2"
            
              advanced:
                repair: true
            
                connection-pool:
                  max-pool-size: 24
                  min-idle: 4
                  max-lifetime: 200000
                  keepalive-time: 70000
                  connection-timeout: 25000
            
                connection-properties:
                  useSSL: false
                  cachePrepStmts: true
            
            messaging:
              enabled: true
              polling-interval: 500
              cleanup-interval: 15000
              type: "sql"
              address: "redis.example.test:6379"
              username: "mq"
              password: "hunter3"
            
              advanced:
                auth-method: "token"
                auth-token: "abc123"
            
                ssl:
                  enabled: true
                  cert-path: "/certs/client.pem"
                  key-path: "/certs/client.key"
                  ca-path: "/certs/ca.pem"
                  verify-server-cert: false
                  verify-hostname: false
            
                rabbitmq:
                  virtual-host: "/mq"
            
                nats:
                  nkey-seed-path: "/nats/seed"
                  jwt-file-path: "/nats/jwt"
                  credentials-path: "/nats/creds"
            """, DatabaseConfig.class);

        assertEquals(DatabaseType.MARIADB, cfg.database.type);
        assertEquals("cosmetics_", cfg.database.tablePrefix);
        assertEquals("db.example.test", cfg.database.host);
        assertEquals(3307, cfg.database.port);
        assertEquals("maquillage", cfg.database.database);
        assertEquals("mq", cfg.database.username);
        assertEquals("hunter2", cfg.database.password);
        assertTrue(cfg.database.advanced.repair);

        assertEquals(24, cfg.database.advanced.connectionPool.maxpoolsize, "a tuned pool size must not reset to the default");
        assertEquals(4, cfg.database.advanced.connectionPool.minidle);
        assertEquals(200000, cfg.database.advanced.connectionPool.maxlifetime);
        assertEquals(70000, cfg.database.advanced.connectionPool.keepalivetime);
        assertEquals(25000, cfg.database.advanced.connectionPool.connectiontimeout);

        assertTrue(cfg.messaging.enabled);
        assertEquals(500, cfg.messaging.pollingInterval);
        assertEquals(15000, cfg.messaging.cleanupInterval);
        // "sql" was BrokerType#getName; the enum constant is what Configurate matches on now
        assertEquals(BrokerType.DATABASE, cfg.messaging.type);
        assertEquals(1, cfg.messaging.addresses.size());
        assertEquals("redis.example.test:6379", cfg.messaging.addresses.getFirst());
        assertEquals("mq", cfg.messaging.username);
        assertEquals("hunter3", cfg.messaging.password);

        assertEquals("token", cfg.messaging.advanced.authMethod);
        assertEquals("abc123", cfg.messaging.advanced.authToken);
        assertTrue(cfg.messaging.advanced.ssl.enabled);
        assertEquals("/certs/client.pem", cfg.messaging.advanced.ssl.certPath);
        assertEquals("/certs/client.key", cfg.messaging.advanced.ssl.keyPath);
        assertEquals("/certs/ca.pem", cfg.messaging.advanced.ssl.caPath);
        assertFalse(cfg.messaging.advanced.ssl.verifyServerCert);
        assertFalse(cfg.messaging.advanced.ssl.verifyHostname);
        assertEquals("/mq", cfg.messaging.advanced.rabbitmq.virtualHost);
        assertEquals("/nats/seed", cfg.messaging.advanced.nats.nkeySeedPath);
        assertEquals("/nats/jwt", cfg.messaging.advanced.nats.jwtFilePath);
        assertEquals("/nats/creds", cfg.messaging.advanced.nats.credentialsPath);

        final String after = Files.readString(dir.resolve("database.yml"));
        assertFalse(after.contains("max-pool-size"));
        assertFalse(after.contains("address:"), "the single address key must be gone, only addresses remains");
    }

    @Test
    @DisplayName("database.yml: the plugin broker type is remapped too")
    void pluginBrokerType(@TempDir Path dir) throws IOException {
        final DatabaseConfig cfg = migrate(dir, "database.yml", """
            messaging:
              type: "plugin"
            """, DatabaseConfig.class);

        assertEquals(BrokerType.PLUGIN_MESSAGING, cfg.messaging.type);
    }

    @Test
    @DisplayName("a missing file is written out with the shipped defaults")
    void defaultsAreWritten(@TempDir Path dir) throws IOException {
        final PluginConfig cfg = new ConfigLoader()
            .withDirectory()
            .withPath(dir.resolve("config.yml"))
            .withHeader("")
            .buildOrThrow(PluginConfig.class);

        assertEquals("en_US", cfg.language);
        assertEquals(20, cfg.module.nickname.length);
        assertTrue(Files.exists(dir.resolve("config.yml")));
    }
}
