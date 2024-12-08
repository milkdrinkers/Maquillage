package io.github.alathra.maquillage.database;

import io.github.alathra.maquillage.database.handler.DatabaseType;

record DatabaseTestParams(String jdbcPrefix, DatabaseType requiredDatabaseType, String tablePrefix) {
}
