package org.cashregister.webapp.persistence.sql;

/**
 * Created by derkhumblet on 25/04/15.
 */
public interface StatisticsQueries {
        static String CUSTOMER_COUNT_QUERY = "" +
            "SELECT subresult.sdate, COUNT(subresult.id)\n" +
            "FROM (\n" +
            "\t SELECT t.id, DATE_FORMAT(t.date, ?1) AS sdate\n" +
            "\t FROM transaction t\n" +
            "\t WHERE t.merchantId = ?2\n" +
            "\t\t AND t.date BETWEEN ?3 AND ?4\n" +
            "\t\t AND t.truck = 0\n " +
            "\t GROUP BY t.id, t.date\n" +
            ") subresult\n" +
            "GROUP BY subresult.sdate\n" +
            "ORDER BY subresult.sdate ASC";

    static String SALE_TURNOVER_QUERY = "" +
            "SELECT subresult.sdate, SUM(subresult.price)\n" +
            "FROM (\n" +
            "\tSELECT t.id, SUM(td.price) AS price, DATE_FORMAT(t.date, ?1) AS sdate\n" +
            "\tFROM transaction t\n" +
            "\t\tJOIN transaction_detail td ON t.id = td.transaction\n" +
            "\tWHERE t.merchantId = ?2\n" +
            "\t\tAND t.date BETWEEN ?3 AND ?4\n" +
            "\t\tAND t.truck = 0\n" +
            "\t\tAND td.price > 0\n" +
            "\tGROUP BY t.id, t.date\n" +
            ") subresult\n" +
            "GROUP BY subresult.sdate\n" +
            "ORDER BY subresult.sdate ASC;";
}
