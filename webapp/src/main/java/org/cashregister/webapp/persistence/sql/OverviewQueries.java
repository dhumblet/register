package org.cashregister.webapp.persistence.sql;

/**
 * Created by derkhumblet on 02/03/15.
 */
public interface OverviewQueries {
    static String STATS_QUERY = "SELECT td.name AS name, p.name AS product, c.name AS category, SUM(td.amount) AS amount, SUM(td.price) AS price\n" +
            "FROM transaction_detail td\n" +
            "\tJOIN transaction t ON t.ID = td.transaction\n" +
            "\tLEFT JOIN product p ON p.ID = td.product\n"  +
            "\tJOIN category c ON c.ID = td.category\n" +
            "WHERE\n" +
            "\tdate >= ?1 AND date <= ?2\n" +
            "\tAND t.merchantId = ?3\n" +
            "\tAND t.truck = ?4 \n" +
            "\t$refunds$\n" +
            "GROUP BY p.ID, c.ID\n" +
            "ORDER BY c.ID, p.name";

    static String STATS_COUNT_QUERY = "SELECT COUNT(*) \n" +
            "FROM \n" +
            "( \n" +
            "SELECT DISTINCT p.ID AS pid, c.ID AS cid \n" +
            "FROM transaction_detail td \n" +
            "JOIN transaction t ON t.ID = td.transaction \n" +
            "LEFT JOIN product p ON p.ID = td.product \n" +
            "JOIN category c ON c.ID = td.category \n" +
            "WHERE \n" +
            "date >= ?1 AND date <= ?2 \n" +
            "AND t.merchantId = ?3 \n" +
            "AND t.truck = ?4 \n" +
            "\t$refunds$\n" +
            ") AS q";

    static String STATS_TOTAL_QUERY = "SELECT SUM(price)\n" +
            "FROM \n" +
            "(\n" +
            "SELECT SUM(td.price) AS price\n" +
            "FROM transaction_detail td\n" +
            "\tJOIN transaction t ON t.ID = td.transaction\n" +
            "\tLEFT JOIN product p ON p.ID = td.product\n" +
            "\tJOIN category c ON c.ID = td.category\n" +
            "WHERE \n" +
            "\tdate >= ?1 AND date <= ?2\n" +
            "\tAND t.merchantId = ?3\n" +
            "\tAND t.truck = ?4 \n" +
            "\t$refunds$\n" +
            "GROUP BY p.ID, c.ID\n" +
            ") q";
}
