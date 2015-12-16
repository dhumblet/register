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
import org.cashregister.webapp.service.api.ConfigService;
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

import static org.cashregister.domain.Transaction.Payment;
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
    @Autowired private ConfigService configService;

    @Override @Transactional
    public Receipt createTransaction(User user, BigDecimal price, Payment paymentType, BigDecimal received, BigDecimal returned, List<RowItem> items) {
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
            createTransaction(user, transactionPrice, paymentType, received, returned, transactionItems, false);
        } else if (!hasTransaction && hasTruck) {
            createTransaction(user, truckPrice, paymentType, received, returned, truckItems, true);
        } else {
            // Transaction
            BigDecimal transactionReturned = received.subtract(transactionPrice);
            createTransaction(user, transactionPrice, paymentType, received, transactionReturned, transactionItems, false);
            // Truck
            createTransaction(user, truckPrice, paymentType, truckPrice, BigDecimal.ZERO, truckItems, true );
        }
        return null;
    }

    private Receipt createTransaction(User user, BigDecimal price, Payment paymentType, BigDecimal received, BigDecimal returned, List<RowItem> items, boolean truck) {
        BigDecimal paymentCost = BigDecimal.ZERO;
        if (paymentType == CARD && price.compareTo(configService.electronicPaymentLimit()) < 0) {
            paymentCost = configService.electronicPaymentCost();
        }
        // Create transaction
        Transaction transaction = Transaction.builder()
                .price(price)
                .received(received)
                .payment(paymentType)
                .paymentCost(paymentCost)
                .returned(returned)
                .truck(truck)
                .user(user)
            .build();

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

        // return receipt;
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
