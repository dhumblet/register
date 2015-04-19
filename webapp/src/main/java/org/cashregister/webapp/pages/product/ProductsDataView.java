package org.cashregister.webapp.pages.product;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.cashregister.webapp.component.CurrencyLabel;
import org.cashregister.domain.Product;
import org.cashregister.webapp.service.api.ProductService;

/**
 * Created by derkhumblet on 12/12/14.
 */
public class ProductsDataView extends DataView<Product> {
    private IModel<ProductsModel> model;
    private WebMarkupContainer container;
    private AjaxButton editSave, delete;
    private ProductPageCallback callback;
    private ProductService service;

    public ProductsDataView(String id,
                            IDataProvider<Product> provider,
                            IModel<ProductsModel> model,
                            WebMarkupContainer container,
                            ProductService service,
                            ProductPageCallback callback) {
        super(id, provider);
        this.model = model;
        this.container = container;
        this.service = service;
        this.callback = callback;
    }

    @Override
    protected void populateItem(Item<Product> item) {
        IModel<Product> productModel = Model.of(item.getModelObject());

        item.add(new Label("serial", new PropertyModel(productModel, "serial")));
        item.add(new Label("category", new PropertyModel(productModel, "category.name")));
        item.add(new Label("name", new PropertyModel(productModel, "name")));
        item.add(new CurrencyLabel("price", new PropertyModel(productModel, "price")));

        Form editForm = new Form("editForm");
        populateEditButton(item, editForm, productModel);
        populateDeleteButton(item, editForm, productModel);
        item.add(editForm.setOutputMarkupId(true));

    }

    private void populateEditButton(Item<Product> item, Form editForm, final IModel<Product> productModel) {
        final Product product = item.getModelObject();

        editForm.add(new AjaxButton("edit", editForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                model.getObject().setEditProductId(product.getId());
                target.add(container);
                callback.edit(target);
            }
        });
    }

    private void populateDeleteButton(Item<Product> item, Form editForm, IModel<Product> productModel) {
        final Product product = item.getModelObject();
        editForm.add(new AjaxButton("remove", editForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                service.delete(product);
                target.add(container);
            }
        });
    }


}
