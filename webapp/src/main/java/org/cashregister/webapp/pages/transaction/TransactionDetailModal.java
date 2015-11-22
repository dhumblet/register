package org.cashregister.webapp.pages.transaction;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.webapp.component.CurrencyLabel;
import org.cashregister.webapp.component.Modal;
import org.cashregister.webapp.component.ModalCallback;
import org.cashregister.domain.Transaction;
import org.cashregister.domain.TransactionDetail;
import org.cashregister.webapp.persistence.api.TransactionDetailRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by derkhumblet on 23/12/14.
 */
public class TransactionDetailModal extends Modal {
    private AjaxButton close;
    private IModel<Transaction> model;
    private List<IModel<TransactionDetail>> details = new ArrayList<IModel<TransactionDetail>>();
    @SpringBean private TransactionDetailRepository detailRepo;

    public TransactionDetailModal(String id, Form form, IModel<Transaction> model, ModalCallback callback) {
        super(id, form, callback);
        this.model = model;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        getForm().add(new RefreshingView<TransactionDetail>("details") {
            @Override
            protected void populateItem(Item<TransactionDetail> item) {
                final TransactionDetail detail = item.getModel().getObject();
                item.add(new Label("name", detail.name()));
                item.add(new Label("amount", detail.amount()));
                item.add(new CurrencyLabel("price", Model.of(detail.price())));
            }

            @Override
            protected Iterator<IModel<TransactionDetail>> getItemModels() {
                return details.iterator();
            }
        });

        getForm().add(new CurrencyLabel("total", new PropertyModel(model, "price")));

        /* CLOSE */
        close = new AjaxButton("close", getForm()) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                callback.close(target);
            }
        };
        getForm().add(close.setOutputMarkupId(true));

    }

    @Override
    public void onConfigure() {
        super.onConfigure();
        details.clear();
        if (model.getObject().getId() != null && model.getObject().getId() > 0) {
            for (TransactionDetail detail : detailRepo.findByTransaction(model.getObject().getId())) {
                details.add(Model.of(detail));
            }
        }
    }

    public Component getFocusElement() {
        return close;
    }
}
