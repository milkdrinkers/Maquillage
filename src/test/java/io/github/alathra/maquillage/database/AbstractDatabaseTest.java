package io.github.alathra.maquillage.database;

import io.github.alathra.maquillage.database.config.DatabaseConfig;
import io.github.alathra.maquillage.database.exception.DatabaseInitializationException;
import io.github.alathra.maquillage.database.sync.SyncHandler;
import io.github.alathra.maquillage.utility.DB;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.alathra.maquillage.database.schema.Tables.COLORS;
import static io.github.alathra.maquillage.database.schema.tables.Tags.TAGS;

/**
 * Contains all test cases.
 */
@Tag("database")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class AbstractDatabaseTest {
    private final DatabaseTestParams testConfig;
    public DatabaseConfig databaseConfig;
    public Logger logger = LoggerFactory.getLogger("Database Test Logger");

    AbstractDatabaseTest(DatabaseTestParams testConfig) {
        this.testConfig = testConfig;
    }

    /**
     * Exposes the database parameters of this test.
     * @return the database test config
     */
    public DatabaseTestParams getTestConfig() {
        return testConfig;
    }

    @BeforeEach
    void beforeEachTest() {
    }

    @AfterEach
    void afterEachTest() {
    }

    @AfterAll
    void afterAllTests() {
        DB.getHandler().shutdown(); // Shut down the connection pool after all tests have been run
    }

    @Test
    @Order(1) // This forces migrations to be run before any other queries are tested (User queries won't work if the migrations failed)
    @DisplayName("Flyway migrations")
    void testMigrations() throws DatabaseInitializationException {
        DB.getHandler().migrate();
    }

    @Test
    @DisplayName("Create Tags")
    void testInsertReturningTag() {
        for (int i = 1; i < 6; i++) {
            final int databaseId = Queries.Tag.saveTag("<red>[Test]", "maquillage.tag.test", "Test");
            Assertions.assertNotEquals(-1, databaseId);
            Assertions.assertEquals(i, databaseId);

            Queries.Sync.saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.TAG, databaseId);
        }
    }

    @Test
    @DisplayName("Create Namecolors")
    void testInsertReturningNamecolor() {
        for (int i = 1; i < 6; i++) {
            final int databaseId = Queries.NameColor.saveColor("<red>", "maquillage.tag.test", "Test");
            Assertions.assertNotEquals(-1, databaseId);
            Assertions.assertEquals(i, databaseId);

            Queries.Sync.saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.COLOR, databaseId);
        }
    }

    @Test
    @DisplayName("Select All Tags")
    void testFetchAllTags() {
        var result = Queries.Tag.loadAllTags();
        Assertions.assertNotNull(result, "The fetched jOOQ Results are null!");

        int startingInt = 0;
        for (var _record : result) {
            startingInt++;
            Assertions.assertEquals(startingInt, _record.get(TAGS.ID));
            Assertions.assertEquals("<red>[Test]", _record.get(TAGS.TAG));
            Assertions.assertEquals("maquillage.tag.test", _record.get(TAGS.PERM));
            Assertions.assertEquals("Test", _record.get(TAGS.LABEL));
        }
    }

    @Test
    @DisplayName("Select All Namecolors")
    void testFetchAllNamecolors() {
        var result = Queries.NameColor.loadAllColors();
        Assertions.assertNotNull(result, "The fetched jOOQ Results are null!");

        int startingInt = 0;
        for (var _record : result) {
            startingInt++;
            Assertions.assertEquals(startingInt, _record.get(COLORS.ID));
            Assertions.assertEquals("<red>", _record.get(COLORS.COLOR));
            Assertions.assertEquals("maquillage.tag.test", _record.get(COLORS.PERM));
            Assertions.assertEquals("Test", _record.get(COLORS.LABEL));
        }
    }

//    @Test
//    @DisplayName("Batch")
//    void testQueryBatch() {
//        Queries.saveAll();
//    }
//
//    @Test
//    @DisplayName("Transaction")
//    void testQueryTransaction() {
//        Queries.saveAllTransaction();
//    }
//
//    @Test
//    @DisplayName("Select")
//    void testQuerySelect() {
//        Queries.loadAll();
//    }
}
