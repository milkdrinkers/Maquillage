CREATE TABLE IF NOT EXISTS "${tablePrefix}tags" (
    "id" int NOT NULL AUTO_INCREMENT,
    "tag" tinytext NOT NULL,
    "perm" tinytext,
    PRIMARY KEY ("id")
)${tableDefaults};


CREATE TABLE IF NOT EXISTS "${tablePrefix}colors" (
    "id" int NOT NULL AUTO_INCREMENT,
    "color" tinytext NOT NULL,
    "perm" tinytext,
    PRIMARY KEY ("id")
)${tableDefaults};

CREATE TABLE IF NOT EXISTS "${tablePrefix}tags_players" (
    "player" ${uuidType} NOT NULL,
    "tag" int NOT NULL,
    PRIMARY KEY ("player")
)${tableDefaults};


CREATE TABLE IF NOT EXISTS "${tablePrefix}colors_players" (
    "player" ${uuidType} NOT NULL,
    "color" int NOT NULL,
    PRIMARY KEY ("player")
)${tableDefaults};
