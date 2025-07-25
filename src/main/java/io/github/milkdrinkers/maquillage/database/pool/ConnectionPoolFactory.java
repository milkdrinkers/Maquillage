package io.github.milkdrinkers.maquillage.database.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.milkdrinkers.maquillage.database.config.DatabaseConfig;
import io.github.milkdrinkers.maquillage.database.exception.DatabaseInitializationException;
import org.slf4j.Logger;

public final class ConnectionPoolFactory {
    public static HikariDataSource create(DatabaseConfig databaseConfig, Logger logger) throws DatabaseInitializationException {
        try {
            HikariConfig hikariConfig = ConnectionPoolConfigFactory.get(databaseConfig);

            return new HikariDataSource(hikariConfig);
        } catch (Throwable t) {
            throw new DatabaseInitializationException("Failed to initialize database pool during startup. Are you using the correct database type?");
        }
    }
}
