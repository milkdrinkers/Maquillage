CREATE TABLE IF NOT EXISTS "${tablePrefix}cooldowns" (
      "uuid" BINARY(16) NOT NULL,
      "cooldown_type" TEXT NOT NULL,
      "cooldown_time" TIMESTAMP NOT NULL
);