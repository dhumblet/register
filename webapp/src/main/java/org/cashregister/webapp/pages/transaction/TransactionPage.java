package org.cashregister.webapp.pages.transaction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.webapp.component.CustomDateConverter;
import org.cashregister.webapp.component.Modal;
import org.cashregister.webapp.component.ModalCallback;
import org.cashregister.domain.Transaction;
import org.cashregister.webapp.pages.template.SecureTemplatePage;
import org.cashregister.webapp.service.api.TransactionService;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private IModel<Date> transactionDate;
    private IModel<String> printableDates;

    @Override
    public String getTitle() {
        return getString("menu.transactions");
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        transaction = Model.of(new Transaction());
        transactionDate = Model.of(new Date());

        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupId(true));

        printableDates = Model.of(dateToString(transactionDate.getObject()));
        // Dates
        initPrintableDates();
        initDateButton(true);
        initDateButton(false);
        initTransactionDatePicker();
        // Data
        initTable();
        initTransactionDetail();
    }


    private void initTransactionDatePicker() {
        //Date field
        DateTextField dateTextField = new DateTextField("transactionsDate", transactionDate, new CustomDateConverter());

        DatePicker datePicker = new DatePicker();
        datePicker.setShowOnFieldClick(true);
        datePicker.setAutoHide(true);
        dateTextField.add(datePicker);
        dateTextField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                calculatePrintableDates();
                target.add(container);
            }
        });
        dateTextField.setMarkupId("statsstartdate");
        container.add(dateTextField.setOutputMarkupId(true));
    }

    private void initDateButton(final boolean prev) {
        Form form = new Form(prev ? "prevForm" : "nextForm");
        form.add(new AjaxButton(prev ? "prevButton" : "nextButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                updateDate(prev);
                target.add(container);
            }
        }.setOutputMarkupId(true));
        container.add(form.setOutputMarkupId(true));
    }

    private void initTable() {
        table = new WebMarkupContainer("table");
        container.add(table.setOutputMarkupId(true));

        transactions = new TransactionsDataView("transactions", new TransactionProvider(service, getMerchantId(), transactionDate), new Callback() {
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

    private void initPrintableDates() {
        calculatePrintableDates();
        container.add(new Label("printableDates", printableDates));
    }

    private void calculatePrintableDates() {
        StringBuilder dates = new StringBuilder();
        dates.append(dateToString(transactionDate.getObject()));
        printableDates.setObject(dates.toString());
    }

    private String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy", getRegisterSession().getLocale());
        return df.format(date);
    }

    private void updateDate(boolean prev) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(transactionDate.getObject());
        cal.add(Calendar.DATE, prev ? -1 : 1);
        transactionDate.setObject(cal.getTime());
    }
}
