-- Version 1.3
ALTER TABLE user ADD CONSTRAINT role_FK FOREIGN KEY (roleId) REFERENCES role(id) ON DELETE SET NULL ON UPDATE NO ACTION;

CREATE TABLE config (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  merchant bigint(20) NOT NULL,
  config_key varchar(255) NOT NULL,
  config_value varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE config ADD CONSTRAINT merchant_FK FOREIGN KEY (merchant) REFERENCES merchant(id) ON DELETE CASCADE ON UPDATE CASCADE;
INSERT INTO config (merchant, config_key, config_value) VALUES
    ('2',  'electronic_payment_limit',  '10'),
    ('2',  'electronic_payment_cost',  '0.20');

ALTER TABLE  transaction ADD paymentCost DECIMAL NOT NULL DEFAULT '0' AFTER payment;
