CREATE TABLE IF NOT EXISTS "${tablePrefix}tags" (
    "id" INT AUTO_INCREMENT,
    "tag" TINYTEXT NOT NULL,
    "perm" TINYTEXT,
    "label" TINYTEXT,
    "key" TINYTEXT,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "${tablePrefix}colors" (
    "id" INT AUTO_INCREMENT,
    "color" TINYTEXT NOT NULL,
    "perm" TINYTEXT,
    "label" TINYTEXT,
    "key" TINYTEXT,
    PRIMARY KEY ("id")
);

-- Create a user table with a auto-incrementing column, and unique column, and named foreign key constraint on column
CREATE TABLE IF NOT EXISTS "${tablePrefix}tags_players" (
    "player" BINARY(16) NOT NULL,
    "tag" INT NOT NULL,
    PRIMARY KEY ("player"),
    CONSTRAINT "${tablePrefix}tags_players_tag_foreign_key" FOREIGN KEY ("tag") REFERENCES "${tablePrefix}tags" ("id") ON DELETE CASCADE
);
CREATE INDEX "${tablePrefix}tags_players_tag_index" ON "${tablePrefix}tags_players" ("tag"); -- Indexes and Unique indexed must be created in separate statements due to SQLite

-- Create a user table with a auto-incrementing column, and unique column, and named foreign key constraint on column
CREATE TABLE IF NOT EXISTS "${tablePrefix}colors_players" (
    "player" BINARY(16) NOT NULL,
    "color" INT NOT NULL,
    PRIMARY KEY ("player"),
    CONSTRAINT "${tablePrefix}colors_players_color_foreign_key" FOREIGN KEY ("color") REFERENCES "${tablePrefix}colors" ("id") ON DELETE CASCADE
);
CREATE INDEX "${tablePrefix}colors_players_color_index" ON "${tablePrefix}colors_players" ("color"); -- Indexes and Unique indexed must be created in separate statements due to SQLite
