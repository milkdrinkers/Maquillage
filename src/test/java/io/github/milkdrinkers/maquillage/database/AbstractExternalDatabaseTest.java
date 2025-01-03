package io.github.milkdrinkers.maquillage.database;

import io.github.milkdrinkers.maquillage.database.config.DatabaseConfigBuilder;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseHandlerBuilder;
import io.github.milkdrinkers.maquillage.utility.DB;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("externaldatabase")
@Testcontainers(disabledWithoutDocker = true)
public abstract class AbstractExternalDatabaseTest extends AbstractDatabaseTest {
    @Container
    public static GenericContainer<?> container;

    AbstractExternalDatabaseTest(GenericContainer<?> container, DatabaseTestParams testConfig) {
        super(testConfig);
        AbstractExternalDatabaseTest.container = container;
        container.start();
    }

    @BeforeAll
    @DisplayName("Initialize connection pool")
    void beforeAllTests() {
        Assertions.assertTrue(container.isRunning());

        databaseConfig = new DatabaseConfigBuilder()
            .withDatabaseType(getTestConfig().jdbcPrefix())
            .withDatabase("testing")
            .withHost(container.getHost())
            .withPort(container.getFirstMappedPort())
            .withUsername("root")
            .withPassword("")
            .withTablePrefix(getTestConfig().tablePrefix())
            .build();
        Assertions.assertEquals(getTestConfig().requiredDatabaseType(), databaseConfig.getDatabaseType());

        DB.init(
            new DatabaseHandlerBuilder()
                .withDatabaseConfig(databaseConfig)
                .withLogger(logger)
                .build()
        );
        DB.getHandler().startup();
    }

    @AfterAll
    void afterAllTests() {
        container.stop();
    }
}
