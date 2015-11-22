package org.cashregister.webapp.pages.transaction;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.Transaction;
import org.cashregister.webapp.service.api.TransactionService;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by derkhumblet on 12/12/14.
 */
public class TransactionProvider implements IDataProvider<Transaction> {
    @SpringBean private TransactionService service;
    private long merchantId;
    private IModel<Date> transactionDate;

    public TransactionProvider(TransactionService service, long merchantId, IModel<Date> transactionDate) {
        this.service = service;
        this.merchantId = merchantId;
        this.transactionDate = transactionDate;
    }

    @Override
    public long size() {
        return service.countTodayTransactions(merchantId, transactionDate.getObject());
    }

    @Override
    public Iterator<Transaction> iterator(long first, long count) {
        return service.getTransactionsForDate(merchantId, transactionDate.getObject()).iterator();
//        return service.getTodayTransactions(merchantId).iterator();
    }

    @Override
    public IModel<Transaction> model(Transaction transaction) {
        return Model.of(transaction);
    }

    @Override
    public void detach() { }
}
