package org.cashregister.webapp.pages.transaction;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.cashregister.domain.Transaction;
import org.cashregister.webapp.component.CurrencyLabel;
import org.cashregister.webapp.util.DateHelper;

/**
 * Created by derkhumblet on 28/12/14.
 */
public class TransactionsDataView extends DataView<Transaction> {
    private TransactionPage.Callback callback;

    public TransactionsDataView(String id, IDataProvider<Transaction> provider, TransactionPage.Callback callback) {
        super(id, provider);
        this.callback = callback;
    }

    @Override
    protected void populateItem(Item<Transaction> item) {
        final Transaction transaction = item.getModelObject();
        item.add(new Label("date", DateHelper.getDateString(transaction.date())));
        item.add(new Label("time", DateHelper.getTimeString(transaction.date())));
        item.add(new CurrencyLabel("amount", Model.of(transaction.price())));
        item.add(new CurrencyLabel("given", Model.of(transaction.received())));
        item.add(new Label("type", transaction.payment().representation()));
        item.add(new CurrencyLabel("returned", Model.of(transaction.returned())));

        item.add(new AjaxEventBehavior("onClick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                callback.showTransaction(target, transaction.getId());
            }
        });
    }
}
