package io.github.milkdrinkers.maquillage.database;

import io.github.milkdrinkers.maquillage.database.handler.DatabaseType;

record DatabaseTestParams(String jdbcPrefix, DatabaseType requiredDatabaseType, String tablePrefix) {
}
