package org.cashregister.webapp.pages.product;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.Merchant;
import org.cashregister.domain.Product;
import org.cashregister.webapp.behavior.FocusOnLoadBehavior;
import org.cashregister.webapp.behavior.PlaceholderBehavior;
import org.cashregister.webapp.behavior.ShortcutBehavior;
import org.cashregister.webapp.component.CustomFeedbackPanel;
import org.cashregister.webapp.component.Modal;
import org.cashregister.webapp.component.ModalCallback;
import org.cashregister.webapp.navigator.PagingNavigator;
import org.cashregister.webapp.pages.template.SecureTemplatePage;
import org.cashregister.webapp.persistence.api.ProductRepository;
import org.cashregister.webapp.service.api.CategoryService;
import org.cashregister.webapp.service.api.ProductService;
import org.cashregister.webapp.util.ProductHelper;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by derkhumblet on 11/12/14.
 */
public class ProductPage extends SecureTemplatePage {
    private static final int ITEMS_PER_PAGE = 10;
    @SpringBean private ProductRepository repo;
    @SpringBean private CategoryService categoryService;
    @SpringBean private ProductService productService;
    private WebMarkupContainer container, table;
    private IModel<ProductsModel> model;
    private IModel<Product> productModel;
    private Form searchForm, productForm;
    private ProductsDataView products;
    private AjaxButton newProduct;

    private Modal productModal;
    private FeedbackPanel modalFeedback;

    @Override
    protected String getTitle() {
        return getString("menu.products");
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        model = Model.of(new ProductsModel());
        productModel = Model.of(new Product());
        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupId(true));

        initSearchField();
        initTable();
        initProductModal();
        initShortcuts();
    }

    @Override
    public void onConfigure() {
        super.onConfigure();
    }

    private void initSearchField() {
        searchForm = new Form("searchForm", new CompoundPropertyModel(model));
        container.add(searchForm.setOutputMarkupId(true));

        TextField searchField = new TextField("search");
        searchField.add(PlaceholderBehavior.withText(getString("input.placeholder")));
        searchField.add(new FocusOnLoadBehavior());

        newProduct = new AjaxButton("newProduct", searchForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                model.getObject().setEditProductId(-1L);
                productModel.setObject(new Product());
                target.appendJavaScript(Modal.showAndFocus(productModal, productModal.getFocusElement()));
                target.add(productForm);
            }
        };

        searchForm.add(newProduct.setOutputMarkupId(true));
        searchForm.add(searchField.setOutputMarkupId(true));
    }

    private void initTable() {
        table = new WebMarkupContainer("table");
        container.add(table.setOutputMarkupId(true));

        products = new ProductsDataView("products", new ProductProvider(repo, model, getMerchantId()), model, container, productService, new ProductPageCallback() {
            @Override
            public void edit(AjaxRequestTarget target) {
                Long editId = model.getObject().getEditProductId();
                if (editId != null) {
                    productModel.setObject(getProduct(editId));
                } else {
                    productModel.setObject(new Product());
                }
                target.appendJavaScript(Modal.showAndFocus(productModal, productModal.getFocusElement()));
                target.add(productForm);
            }
        });

        products.setItemsPerPage(ITEMS_PER_PAGE);
        table.add(products.setOutputMarkupId(true));

        container.add(new PagingNavigator("pagerBottom", products).setAutoHide(true));
    }

    private void initProductModal() {
        productForm = new Form("product-form",new CompoundPropertyModel(productModel));
        modalFeedback = new CustomFeedbackPanel("productFeedback");
        productForm.add(modalFeedback.setOutputMarkupPlaceholderTag(true));

        productModal = new ProductModal("productModal", productForm, getMerchantId(), new ModalCallback() {
            @Override
            public void ok(AjaxRequestTarget target) {
                final Product product = productModel.getObject();
                product.setMerchant(getMerchant());
                if (validate(product, target)) {
                    if (product.getId() != null && product.getId() > -1) {
                        productService.update(product);
                    } else {
                        productService.create(product);
                    }
                    model.getObject().resetIds();
                    productModel.setObject(new Product());
                    target.add(container, productModal);
                    target.appendJavaScript(Modal.hide(productModal));
                } else {
                    target.add(modalFeedback);
                }
            }

            @Override
            public void close(AjaxRequestTarget target) {
                model.getObject().resetIds();
                target.add(container, productModal);
                target.appendJavaScript(Modal.hide(productModal));
            }
        });
        add(productModal.setOutputMarkupId(true));
    }

    private boolean validate(Product product, AjaxRequestTarget target) {
        if (product.getSerial() == null || product.getSerial().isEmpty()) {
            productForm.error(getString("error.required.barcode"));
            focus(target, "serial");
            return false;
        } else if (!ProductHelper.isProduct(product.getSerial())) {
            productForm.error(getString("error.invalid.barcode"));
            focus(target, "serial");
            return false;
        } else if (product.getId() == null && productService.findProduct(product.getSerial(), getMerchantId()) != null) {
            productForm.error(getString("error.used.barcode") + " '" + productService.findProduct(product.getSerial(), getMerchantId()).getName() + "'");
            focus(target, "serial");
            return false;
        } else if (product.getName() == null || product.getName().isEmpty()) {
            productForm.error(getString("error.required.name"));
            focus(target, "name");
            return false;
        } else if (product.getCategory() == null) {
            productForm.error(getString("error.required.category"));
            focus(target, "category");
            return false;
        } else if (product.getPrice() == null) {
            productForm.error(getString("error.required.price"));
            focus(target, "price");
            return false;
        } else if (product.getMerchant() == null) {
            productForm.error(getString("error.required.merchant"));
            return false;
        }
        return true;
    }

    private void focus(AjaxRequestTarget target, String id) {
        target.appendJavaScript("$(\"#" + id + "\").focus();");
    }

    private void initShortcuts() {
        ShortcutBehavior shortcutBehavior = new ShortcutBehavior();
        shortcutBehavior.addShortcut("esc", "$(\"#search\").focus();");
        add(shortcutBehavior);
    }

    @Transactional
    public Product getProduct(Long id) {
        return repo.findProduct(id);
    }

    private long getProductsSize() {
        return products.getDataProvider().size();
    }
}
