package org.cashregister.webapp.pages.transaction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.webapp.component.Modal;
import org.cashregister.webapp.component.ModalCallback;
import org.cashregister.domain.Transaction;
import org.cashregister.webapp.pages.template.SecureTemplatePage;
import org.cashregister.webapp.service.api.TransactionService;

import java.io.Serializable;

/**
 * Created by derkhumblet on 28/12/14.
 */
public class TransactionPage extends SecureTemplatePage {
    @SpringBean private TransactionService service;
    private WebMarkupContainer container, table;
    private TransactionsDataView transactions;
    private Modal transactionDetail;
    private IModel<Transaction> transaction;
    private Form detailForm;

    @Override
    public String getTitle() {
        return getString("menu.transactions");
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        transaction = Model.of(new Transaction());

        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupId(true));

        initTable();
        initTransactionDetail();
    }

    private void initTable() {
        table = new WebMarkupContainer("table");
        container.add(table.setOutputMarkupId(true));

        transactions = new TransactionsDataView("transactions", new TransactionProvider(service, getMerchantId()), new Callback() {
            @Override
            public void showTransaction(AjaxRequestTarget target, long id) {
                transaction.setObject(service.getTransaction(id));
                target.appendJavaScript(Modal.showAndFocus(transactionDetail, transactionDetail.getFocusElement()));
                target.add(transactionDetail);
            }
        });

        table.add(transactions.setOutputMarkupId(true));
    }

    private void initTransactionDetail() {
        detailForm = new Form("detailForm");
        transactionDetail = new TransactionDetailModal("detailModal", detailForm, transaction, new ModalCallback() {
            @Override
            public void ok(AjaxRequestTarget target) { }

            @Override
            public void close(AjaxRequestTarget target) {
                transaction.setObject(new Transaction());
                target.appendJavaScript(Modal.hide(transactionDetail));
                target.add(detailForm);
            }
        });
        add(transactionDetail.setOutputMarkupId(true));
    }

    public interface Callback extends Serializable {
        void showTransaction(AjaxRequestTarget target, long id);
    }
}
