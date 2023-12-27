CREATE TABLE IF NOT EXISTS "${tablePrefix}tags" (
    "id" int NOT NULL AUTO_INCREMENT,
    "tag" tinytext NOT NULL,
    "perm" tinytext,
    "displayname" tinytext,
    PRIMARY KEY ("id")
)${tableDefaults};


CREATE TABLE IF NOT EXISTS "${tablePrefix}colors" (
    "id" int NOT NULL AUTO_INCREMENT,
    "color" tinytext NOT NULL,
    "perm" tinytext,
    "displayname" tinytext,
    PRIMARY KEY ("id")
)${tableDefaults};

CREATE TABLE IF NOT EXISTS "${tablePrefix}tags_players" (
    "player" ${uuidType} NOT NULL,
    "tag" int NOT NULL,
    PRIMARY KEY ("player"),
    INDEX "tagid" ("tag"),
    CONSTRAINT "tagid" FOREIGN KEY ("tag") REFERENCES ${tablePrefix}tags (id) ON DELETE CASCADE
)${tableDefaults};


CREATE TABLE IF NOT EXISTS "${tablePrefix}colors_players" (
    "player" ${uuidType} NOT NULL,
    "color" int NOT NULL,
    PRIMARY KEY ("player"),
    INDEX "colorid" ("color"),
    CONSTRAINT "colorid" FOREIGN KEY ("color") REFERENCES ${tablePrefix}colors (id) ON DELETE CASCADE
)${tableDefaults};
