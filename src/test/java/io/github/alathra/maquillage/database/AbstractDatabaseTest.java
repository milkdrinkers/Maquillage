package io.github.alathra.maquillage.database;

import io.github.alathra.maquillage.database.handler.DatabaseHandler;
import io.github.alathra.maquillage.database.config.DatabaseConfig;
import io.github.alathra.maquillage.database.exception.DatabaseMigrationException;
import io.github.alathra.maquillage.database.migration.MigrationHandler;
import io.github.alathra.maquillage.database.jooq.JooqContext;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static io.github.alathra.maquillage.database.schema.Tables.COLORS;

@Tag("database")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class AbstractDatabaseTest {
    public String jdbcPrefix;
    public DatabaseType requiredDatabaseType;
    public DatabaseConfig databaseConfig;
    public DatabaseHandler databaseHandler;
    public Logger logger = LoggerFactory.getLogger("Database Test Logger");
    @SuppressWarnings("unused")
    static List<String> tablePrefixes = Arrays.asList("", "test_", "somelongprefix_");

    public AbstractDatabaseTest(String jdbcPrefix, DatabaseType requiredDatabaseType) {
        this.jdbcPrefix = jdbcPrefix;
        this.requiredDatabaseType = requiredDatabaseType;
    }

    @BeforeEach
    void beforeEachTest() {
    }

    @AfterEach
    void afterEachTest() {
    }

    @AfterAll
    void afterAllTests() {
        databaseHandler.shutdown();
    }

    // Shared tests

    @ParameterizedTest
    @FieldSource("tablePrefixes")
    @Order(1)
    @DisplayName("Flyway migrations")
    void testMigrations(String prefix) throws DatabaseMigrationException {
        databaseHandler.getDatabaseConfig().setTablePrefix(prefix);
        try {
            new MigrationHandler(
                databaseHandler.getConnectionPool(),
                databaseHandler.getDatabaseConfig()
            )
                .migrate();
        } catch (Exception e) {
            logger.error("Database migration failed!", e.fillInStackTrace());
        }
    }

    @ParameterizedTest
    @FieldSource("tablePrefixes")
    @DisplayName("Select query")
    void testQuerySelect(String prefix) throws SQLException {
        databaseHandler.getDatabaseConfig().setTablePrefix(prefix);
        JooqContext jooqContext = new JooqContext(databaseHandler.getDatabaseConfig());

        Connection con = databaseHandler.getConnection();
        DSLContext context = jooqContext.createContext(con);
        context
            .select(COLORS.fields(COLORS.COLOR, COLORS.PERM, COLORS.LABEL, COLORS.KEY))
            .from(COLORS)
            .where(COLORS.ID.equal(1))
            .fetchOne();
        con.close();
    }

    @ParameterizedTest
    @FieldSource("tablePrefixes")
    @DisplayName("Insert query")
    void testQueryInsert(String prefix) throws SQLException {
        databaseHandler.getDatabaseConfig().setTablePrefix(prefix);
        JooqContext jooqContext = new JooqContext(databaseHandler.getDatabaseConfig());

        Connection con = databaseHandler.getConnection();
        DSLContext context = jooqContext.createContext(con);
        Record1<Integer> record = context
            .insertInto(COLORS, COLORS.COLOR, COLORS.PERM, COLORS.LABEL, COLORS.KEY)
            .values(
                "<blue>",
                "",
                "testname",
                "testidentifier"
            )
            .returningResult(COLORS.ID)
            .fetchOne();
        con.close();
    }

    @ParameterizedTest
    @FieldSource("tablePrefixes")
    @DisplayName("Set query")
    void testQuerySet(String prefix) throws SQLException {
        databaseHandler.getDatabaseConfig().setTablePrefix(prefix);
        JooqContext jooqContext = new JooqContext(databaseHandler.getDatabaseConfig());

        Connection con = databaseHandler.getConnection();
        DSLContext context = jooqContext.createContext(con);
        context.update(COLORS)
            .set(COLORS.COLOR, "<red>")
            .set(COLORS.PERM, "someperm")
            .set(COLORS.LABEL, "testname")
            .set(COLORS.KEY, "testidentifier")
            .where(COLORS.ID.eq(1))
            .execute();
        con.close();
    }
}
