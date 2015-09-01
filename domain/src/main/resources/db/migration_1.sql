ALTER TABLE category ADD merchantId BIGINT NOT NULL AFTER id;
UPDATE category SET merchantId = 2;