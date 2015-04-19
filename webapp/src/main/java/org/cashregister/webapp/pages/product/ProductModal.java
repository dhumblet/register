package org.cashregister.webapp.pages.product;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.Category;
import org.cashregister.domain.Product;
import org.cashregister.webapp.component.Modal;
import org.cashregister.webapp.component.ModalCallback;
import org.cashregister.webapp.service.api.CategoryService;
import org.cashregister.webapp.util.ProductHelper;

/**
 * Created by derkhumblet on 23/12/14.
 */
public class ProductModal extends Modal {
    private TextField productSerial, productName, productPrice;
    private DropDownChoice<Category> productCategory;
    private long merchantId;

    @SpringBean
    private CategoryService categoryService;

    public ProductModal(String id, Form form, long merchantId, ModalCallback callback) {
        super(id, form, callback);
        this.merchantId = merchantId;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        /* SERIAL */
        productSerial = new TextField("serial");
        getForm().add(productSerial.setOutputMarkupId(true));

        /* NAME */
        productName = new TextField("name");
        getForm().add(productName.setOutputMarkupId(true));

        /* PRICE */
        productPrice = new TextField("price");
        getForm().add(productPrice.setOutputMarkupId(true));

        /* CATEGORY */
        productCategory = new DropDownChoice<Category>("category", categoryService.getCategories(merchantId), new IChoiceRenderer<Category>() {
            @Override
            public Object getDisplayValue(Category object) {
                return object.getName();
            }

            @Override
            public String getIdValue(Category object, int index) {
                return String.valueOf(object.getId());
            }
        });
        getForm().add(productCategory.setOutputMarkupId(true));

        /* CLOSE */
        getForm().add(new AjaxButton("closeProduct", getForm()) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                callback.close(target);
            }
        });

        /* OK */
        getForm().add(new AjaxButton("saveProduct", getForm()) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                callback.ok(target);
            }
        });
    }

    public Component getFocusElement() {
        return productSerial;
    }

    @Override
    public Component getFocusAlternativeElement() {
        return productName;
    }
}