package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Category;
import org.cashregister.domain.Product;
import org.cashregister.domain.Receipt;
import org.cashregister.domain.Transaction;
import org.cashregister.domain.TransactionDetail;
import org.cashregister.domain.User;
import org.cashregister.webapp.pages.kassa.RowItem;
import org.cashregister.webapp.persistence.api.TransactionDetailRepository;
import org.cashregister.webapp.persistence.api.TransactionRepository;
import org.cashregister.webapp.service.api.CategoryService;
import org.cashregister.webapp.service.api.ProductService;
import org.cashregister.webapp.service.api.TransactionService;
import org.cashregister.webapp.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.cashregister.domain.Transaction.Payment.CASH;
import static org.cashregister.domain.Transaction.Payment.CARD;
/**
 * Created by derkhumblet on 11/12/14.
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired private TransactionRepository repo;
    @Autowired private TransactionDetailRepository detailRepo;
    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;

    @Override @Transactional
    public Receipt createTransaction(User user, BigDecimal price, BigDecimal received, BigDecimal returned, List<RowItem> items) {
        boolean hasTruck = false;
        boolean hasTransaction = false;
        List<RowItem> transactionItems = new ArrayList<>();
        List<RowItem> truckItems = new ArrayList<>();
        // Calculate prices and items
        BigDecimal transactionPrice = BigDecimal.ZERO;
        BigDecimal truckPrice = BigDecimal.ZERO;
        for (RowItem item : items) {
            // Filter out products that have been removed
            if (item.getAmount() <= 0) {
                continue;
            }
            if (item.isTruck()) {
                hasTruck = true;
                truckItems.add(item);
                truckPrice = truckPrice.add(item.getTotal());
            } else {
                hasTransaction = true;
                transactionItems.add(item);
                transactionPrice = transactionPrice.add(item.getTotal());
            }
        }
        // Calculate given and returns
        if (hasTransaction && !hasTruck) {
            createTransaction(user, transactionPrice, received, returned, transactionItems, false);
        } else if (!hasTransaction && hasTruck) {
            createTransaction(user, truckPrice, received, returned, truckItems, true);
        } else {
            // Transaction
            BigDecimal transactionReturned = received.subtract(transactionPrice);
            createTransaction(user, transactionPrice, received, transactionReturned, transactionItems, false);
            // Truck
            createTransaction(user, truckPrice, truckPrice, BigDecimal.ZERO, truckItems, true );
        }

//        if (hasTransaction) {
//            createTransaction(user, transactionPrice, received.subtract(truckPrice), returned, transactionItems, false);
//        }
//        if (hasTruck) {
//            final BigDecimal truckReceived = hasTransaction ? received.subtract(transactionPrice).subtract(returned) : received;
//            final BigDecimal truckReturned = hasTransaction ? BigDecimal.ZERO : returned;
//            createTransaction(user, truckPrice, truckReceived, truckReturned, truckItems, true);
//        }
        return null;
    }

    private Receipt createTransaction(User user, BigDecimal price, BigDecimal received, BigDecimal returned, List<RowItem> items, boolean truck) {
        // Create transaction
        Transaction transaction = new Transaction(price, received, CASH, returned, truck);
        transaction.user(user);
        if (user.getMerchant() != null) {
            transaction.merchant(user.getMerchant());
        }
        repo.createTransaction(transaction);
        // Create receipt
//        Receipt receipt = Receipt.forTransaction(transaction);
        // Create details
        for (RowItem item : items) {
            Product product = null;
            Category category = null;
            if (item.isProduct()) {
                product = productService.readProduct(Long.valueOf(item.getId()));
                if (product != null) {
                    category = product.getCategory();
                }
            } else {
                category = categoryService.findCategory(item.getName(), user.getMerchant().getId());
            }
            TransactionDetail detail = new TransactionDetail(category, transaction, product, item.getAmount(), item.getTotal(), truck);
            detailRepo.createTransactionDetail(detail);
//            receipt.addDetail(detail);
        }
//        return receipt;
        return null;
    }

    @Override @Transactional
    public long countTodayTransactions(long merchantId, Date day) {
        Date start = DateHelper.getStartOfDay(day);
        Date end = DateHelper.getEndOfDay(day);
        return  repo.countTransactions(start, end, merchantId);
    }

    @Override @Transactional
    public List<Transaction> getTodayTransactions(long merchantId) {
        Date start = DateHelper.getStartOfDay(new Date());
        Date end = DateHelper.getEndOfDay(new Date());
        return repo.getTransactions(start, end, merchantId);
    }

    @Override @Transactional
    public List<Transaction> getTransactionsForDate(long merchantId, Date day) {
        Date start = DateHelper.getStartOfDay(day);
        Date end = DateHelper.getEndOfDay(day);
        return repo.getTransactions(start, end, merchantId);
    }

    @Override @Transactional
    public Transaction getTransaction(Long id) {
        if (id == null) {
            return null;
        }
        return repo.readTransaction(id);
    }
}
