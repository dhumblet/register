package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.OverviewLine;
import org.cashregister.domain.Transaction;
import org.cashregister.webapp.persistence.sql.OverviewQueries;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by derkhumblet on 11/12/14.
 */
public interface TransactionRepository extends GenericRepository<Transaction>, OverviewQueries {

    /**
     * @param transaction
     */
    void createTransaction(Transaction transaction);

    long countTransactions(Date start, Date end, long merchantId);

    List<Transaction> getTransactions(Date start, Date end, long merchantId);

    Transaction readTransaction(long id);

    long countOverviewLines(Date start, Date end, long merchantId, boolean refunds, boolean truck);

    List<OverviewLine> getOverview(Date start, Date end, long merchantId, boolean refunds, boolean truck);

    BigDecimal getOverviewTotal(Date start, Date end, long merchantId, boolean refunds, boolean truck);
}
