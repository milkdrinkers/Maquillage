package io.github.alathra.maquillage.utility;

import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.database.DatabaseType;
import io.github.alathra.maquillage.database.handler.DatabaseHandler;
import io.github.alathra.maquillage.database.jooq.JooqContext;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Convenience class for accessing methods in {@link DatabaseHandler#getConnection}
 */
public abstract class DB {
    /**
     * Convenience method for {@link DatabaseHandler#getConnection} to getConnection {@link Connection}
     *
     * @return the connection
     * @throws SQLException the sql exception
     */
    @NotNull
    public static Connection getConnection() throws SQLException {
        return Maquillage.getInstance().getDataHandler().getConnection();
    }

    /**
     * Convenience method for {@link JooqContext#createContext(Connection)} to getConnection {@link DSLContext}
     *
     * @param con the con
     * @return the context
     */
    @NotNull
    public static DSLContext getContext(Connection con) {
        return Maquillage.getInstance().getDataHandler().getJooqContext().createContext(con);
    }

    @NotNull
    public static DatabaseHandler getHandler() {
        return Maquillage.getInstance().getDataHandler();
    }

    /**
     * Convenience method for {@link DatabaseHandler#getDB()} to getConnection {@link DatabaseType}
     *
     * @return the database
     */
    public static DatabaseType getDB() {
        return Maquillage.getInstance().getDataHandler().getDB();
    }
}
