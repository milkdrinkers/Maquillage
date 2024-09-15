CREATE TABLE IF NOT EXISTS "${tablePrefix}sync" (
    "id" INT AUTO_INCREMENT,
    "message" TINYTEXT NOT NULL,
    "timestamp" TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
