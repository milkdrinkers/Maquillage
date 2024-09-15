CREATE TABLE IF NOT EXISTS "${tablePrefix}tags" (
    "id" INT AUTO_INCREMENT,
    "tag" TINYTEXT NOT NULL,
    "perm" TINYTEXT,
    "displayname" TINYTEXT,
    "identifier" TINYTEXT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "${tablePrefix}colors" (
    "id" INT AUTO_INCREMENT,
    "color" TINYTEXT NOT NULL,
    "perm" TINYTEXT,
    "displayname" TINYTEXT,
    "identifier" TINYTEXT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "${tablePrefix}tags_players" (
    "player" BINARY(16) NOT NULL,
    "tag" INT NOT NULL,
    PRIMARY KEY (player),
    CONSTRAINT tagid FOREIGN KEY (tag) REFERENCES "${tablePrefix}tags" (id) ON DELETE CASCADE
);
CREATE INDEX tagid ON "${tablePrefix}tags_players" ("tag");

CREATE TABLE IF NOT EXISTS "${tablePrefix}colors_players" (
    "player" BINARY(16) NOT NULL,
    "color" INT NOT NULL,
    PRIMARY KEY (player),
    CONSTRAINT colorid FOREIGN KEY (color) REFERENCES "${tablePrefix}colors" (id) ON DELETE CASCADE
);
CREATE INDEX colorid ON "${tablePrefix}colors_players" ("color");
