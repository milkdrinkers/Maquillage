CREATE TABLE IF NOT EXISTS ${tablePrefix}tags (
    id INT NOT NULL AUTO_INCREMENT,
    tag TINYTEXT NOT NULL,
    perm TINYTEXT,
    "name" TINYTEXT,
    PRIMARY KEY (id)
)${tableDefaults};

CREATE TABLE IF NOT EXISTS ${tablePrefix}colors (
    id INT NOT NULL AUTO_INCREMENT,
    color TINYTEXT NOT NULL,
    perm TINYTEXT,
    "name" TINYTEXT,
    PRIMARY KEY (id)
)${tableDefaults};

CREATE TABLE IF NOT EXISTS ${tablePrefix}tags_players (
    player ${uuidType} NOT NULL,
    tag INT NOT NULL,
    PRIMARY KEY (player),
    INDEX tagid (tag),
    CONSTRAINT tagid FOREIGN KEY (tag) REFERENCES ${tablePrefix}tags (id) ON DELETE CASCADE
)${tableDefaults};

CREATE TABLE IF NOT EXISTS ${tablePrefix}colors_players (
    player ${uuidType} NOT NULL,
    color INT NOT NULL,
    PRIMARY KEY (player),
    INDEX colorid (color),
    CONSTRAINT colorid FOREIGN KEY (color) REFERENCES ${tablePrefix}colors (id) ON DELETE CASCADE
)${tableDefaults};
