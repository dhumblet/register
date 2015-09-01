SELECT t.id, t.date, t.price, td.id, c.name, td.amount, td.price
FROM transaction t
JOIN transaction_detail td ON t.id = td.transaction
JOIN category c ON c.id = td.category
WHERE 1 = 1
	AND t.date BETWEEN '2015-08-1 00:00:00' AND '2015-08-01 23:59:59'
	AND c.name LIKE 'Varia'
