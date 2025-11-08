ALTER TABLE "${tablePrefix}tags"
	ADD COLUMN "weight" INT NOT NULL DEFAULT 0;

ALTER TABLE "${tablePrefix}colors"
	ADD COLUMN "weight" INT NOT NULL DEFAULT 0;