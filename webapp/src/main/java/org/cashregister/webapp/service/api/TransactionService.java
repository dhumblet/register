package org.cashregister.webapp.service.api;

import org.cashregister.domain.Receipt;
import org.cashregister.domain.Transaction;
import org.cashregister.domain.User;
import org.cashregister.webapp.pages.kassa.RowItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import static org.cashregister.domain.Transaction.Payment;
/**
 * Created by derkhumblet on 11/12/14.
 */
public interface TransactionService {
    /**
     * Create a new transaction.
     * @param price
     * @param paymentType
     * @param received
     * @param returned
     * @param details
     */
    Receipt createTransaction(User user, BigDecimal price, Payment paymentType, BigDecimal received, BigDecimal returned, List<RowItem> details);

    long countTodayTransactions(long merchantId, Date day);

    List<Transaction> getTodayTransactions(long merchantId);

    List<Transaction> getTransactionsForDate(long merchantId, Date day);

    Transaction getTransaction(Long id);
}
