package io.github.milkdrinkers.maquillage.database;

import io.github.milkdrinkers.maquillage.database.config.DatabaseConfigBuilder;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseHandlerBuilder;
import io.github.milkdrinkers.maquillage.utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

@Tag("embeddeddatabase")
public abstract class AbstractEmbeddedDatabaseTest extends AbstractDatabaseTest {
    static @TempDir Path TEMP_DIR;

    AbstractEmbeddedDatabaseTest(DatabaseTestParams testConfig) {
        super(testConfig);
    }

    @BeforeAll
    @DisplayName("Initialize connection pool")
    void beforeAllTests() {
        databaseConfig = new DatabaseConfigBuilder()
            .withDatabaseType(getTestConfig().jdbcPrefix())
            .withPath(TEMP_DIR)
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
}
