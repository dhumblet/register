package org.cashregister.webapp.service.api;

import org.cashregister.domain.Receipt;
import org.cashregister.domain.Transaction;
import org.cashregister.domain.User;
import org.cashregister.webapp.pages.kassa.RowItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by derkhumblet on 11/12/14.
 */
public interface TransactionService {
    /**
     * Create a new transaction.
     * @param price
     * @param received
     * @param returned
     * @param details
     */
    Receipt createTransaction(User user, BigDecimal price, BigDecimal received, BigDecimal returned, List<RowItem> details);

    long countTodayTransactions(long merchantId);

    List<Transaction> getTodayTransactions(long merchantId);

    Transaction getTransaction(Long id);
}
