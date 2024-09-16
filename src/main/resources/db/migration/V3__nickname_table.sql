CREATE TABLE IF NOT EXISTS "${tablePrefix}nicknames" (
    "player" BINARY(16) NOT NULL,
    "nickname" TINYTEXT,
    PRIMARY KEY ("player")
);