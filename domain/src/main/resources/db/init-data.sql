--
-- Dumping data for table category
--

INSERT INTO category (id, name, shortcut) VALUES
(1, 'Krant', 'F6'),
(2, 'Magazine', 'F7'),
(3, 'Drank', 'F8'),
(4, 'Geschenk', 'F9'),
(6, 'Sigaretten', 'F10'),
(7, 'Lotto', 'F11'),
(99, 'Varia', 'F12');

--
-- Dumping data for table product
--

INSERT INTO product (id, category, name, price, serial) VALUES
(1, 2, 'Mein Schönes Land', 5.00, '4191744203805'),
(2, 2, 'Schöner Wohnen', 9.00, '4198633009906'),
(3, 2, 'Servus', 5.00, '4192371304507');

INSERT INTO merchant (id, name) VALUES
 (1, 'ROOT');

INSERT INTO user (id, login, password, merchantId, locked) VALUES
(1, 'root', 'd3zsCi3j4su3G5aQIkz6lGh9XcPomrF19alWgYw6hYk=', 1, 0);