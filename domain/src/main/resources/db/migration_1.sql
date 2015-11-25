-- Version 1.2

-- Categories per merchant
ALTER TABLE category ADD merchantId BIGINT NOT NULL AFTER id;
UPDATE category SET merchantId = 2;

-- Save name in transaction detail
ALTER TABLE transaction_detail ADD name VARCHAR(255) NULL AFTER product;
UPDATE transaction_detail td
	LEFT JOIN product p ON td.product = p.id
	LEFT JOIN category c ON td.category = c.id
	SET td.name = IF(td.product IS NULL OR p.name IS NULL, c.name, p.name)


-- Add product/category foreign constraints
ALTER TABLE transaction_detail ADD INDEX(category);
ALTER TABLE transaction_detail ADD INDEX(product);
UPDATE transaction_detail td SET product = NULL WHERE product NOT IN  (SELECT p.id FROM product p);
UPDATE transaction_detail td SET category = 99 WHERE category NOT IN  (SELECT c.id FROM category c);
DELETE FROM transaction_detail WHERE transaction NOT IN  (SELECT t.id FROM transaction t);
ALTER TABLE transaction_detail CHANGE category category BIGINT(20) NULL;
ALTER TABLE transaction_detail ADD CONSTRAINT product_FK FOREIGN KEY (product) REFERENCES product(id) ON DELETE SET NULL ON UPDATE NO ACTION;
ALTER TABLE transaction_detail ADD CONSTRAINT category_FK FOREIGN KEY (category) REFERENCES category(id) ON DELETE SET NULL ON UPDATE NO ACTION;
ALTER TABLE transaction_detail ADD CONSTRAINT transaction_FK FOREIGN KEY (transaction) REFERENCES transaction(id) ON DELETE CASCADE ON UPDATE CASCADE;
