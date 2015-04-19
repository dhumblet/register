package org.cashregister.webapp.pages.kassa;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.Category;
import org.cashregister.domain.Product;
import org.cashregister.domain.Receipt;
import org.cashregister.webapp.behavior.FocusOnLoadBehavior;
import org.cashregister.webapp.behavior.PlaceholderBehavior;
import org.cashregister.webapp.behavior.ReFocusBehavior;
import org.cashregister.webapp.behavior.ShortcutBehavior;
import org.cashregister.webapp.component.CurrencyLabel;
import org.cashregister.webapp.component.CustomFeedbackPanel;
import org.cashregister.webapp.component.Modal;
import org.cashregister.webapp.component.ModalCallback;
import org.cashregister.webapp.pages.product.ProductModal;
import org.cashregister.webapp.pages.product.ProductsModel;
import org.cashregister.webapp.pages.template.SecureTemplatePage;
import org.cashregister.webapp.service.api.CategoryService;
import org.cashregister.webapp.service.api.ProductService;
import org.cashregister.webapp.service.api.TransactionService;
import org.cashregister.webapp.util.BigDecimalHelper;
import org.cashregister.webapp.util.ProductHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by derkhumblet on 10/11/14.
 */
public class KassaPage extends SecureTemplatePage {
    @SpringBean public CategoryService categoryService;
    @SpringBean public ProductService productService;
    @SpringBean public TransactionService transactionService;
    private ReFocusBehavior focusBehavior;
    private IModel<KassaModel> model;
    private WebMarkupContainer container;
    private Form form;
    private TextField productInput;
    private NumberTextField categoryAmountField, totalAmountField;
    private WebMarkupContainer items, categoryAmountModal, totalAmountModal;
    private ShortcutBehavior shortcutBehavior;
    private BigDecimal categoryAmount, totalInputAmount, returnAmount;
    private AjaxButton payButton;
    private Form totalAmountForm;
    private String message;
    // Product modal
    private Form productForm;
    private IModel<ProductsModel> productsModel;
    private IModel<Product> productModel;
    private FeedbackPanel productModalFeedback;
    private Modal productModal;
    private boolean showProductModal = false;

    public KassaPage() {
        super();
    }

    @Override
    protected String getTitle() {
        return getString("menu.kassa");
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        model = Model.of(new KassaModel());
        productsModel = Model.of(new ProductsModel());
        productModel = Model.of(new Product());
        container = new WebMarkupContainer("container");
        shortcutBehavior = new ShortcutBehavior();
        shortcutBehavior.addShortcut("esc", "$(\"#product\").focus();");

        initForm();
        initRowItems();
        initTotal();
        initAmountModal();
        initTotalModal();
        initProductModal();

        // Focus on product input on pageload
        add(new AttributeModifier("onload", "product.focus();"));

        container.add(form.setOutputMarkupPlaceholderTag(true));
        add(container.setOutputMarkupPlaceholderTag(true));
        add(shortcutBehavior);
        focusBehavior = new ReFocusBehavior(productInput);
        container.add(focusBehavior);
    }


    private void initForm() {
        form = new Form<KassaModel>("input-form", new CompoundPropertyModel(model)) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                showProductModal = false;
                String input = productInput.getValue();//model.getObject().getProduct();
                if (ProductHelper.isProduct(input)) {
                    try {
                        Product product = productService.findProduct(input, getMerchantId(), true);
                        if (product != null) {
                            model.getObject().add(product);
                            productInput.clearInput();
                            productInput.setModel(Model.of(""));
                        } else {
                            productNotFound();
                        }
                    } catch (Exception e) {
                        productNotFound();
                    }
                    model.getObject().setProduct(null);
                } else {
                    productInput.setModel(Model.of(""));
                }
                productInput.clearInput();
            }
        };
        form.add(new CustomFeedbackPanel("feedback", new ContainerFeedbackMessageFilter(form)));
        initProductInput();
        initButtons();
        if (message != null) {
            form.success(message);
            message = null;
        }
    }

    private void productNotFound() {
        form.error(getString("error.product_not_found"));
        productModel.setObject(new Product());
        productModel.getObject().setSerial(productInput.getValue());
        productInput.setModel(Model.of(""));
        showProductModal = true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        if (showProductModal) {
            productsModel.getObject().setEditProductId(-1L);
            String script = Modal.showAndFocus(productModal, productModal.getFocusAlternativeElement());
            response.render(OnLoadHeaderItem.forScript(script));
        }
    }

    /* Add shortcut buttons for categories */
    private void initButtons() {
        RepeatingView buttons = new RepeatingView("categoryButtons");
        for (Category category : categoryService.getCategories(getMerchantId())) {
            buttons.add(initButton(buttons.newChildId(), category));
        }
        form.add(buttons.setOutputMarkupId(true));
    }

    /* Add single shortcut button for category */
    private AjaxButton initButton(String wicketId, final Category category) {
        final String name = category.getName();
        final String shortcut = category.getShortcut();
        final String id = "button" + name;
        final String value = name + "\n";
        AjaxButton button = new AjaxButton(wicketId, form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                model.getObject().setProduct(value);
                target.prependJavaScript(Modal.showAndFocus(categoryAmountModal, categoryAmountField));
                target.add(container);
            }
        };
        button.setMarkupId(id);
        button.add(new AttributeModifier("class", "btn btn-info"));
        button.add(new AttributeModifier("style", "margin: 0 2px 2px 0; min-width: 150px;"));

        String labelText;
        if (shortcut == null || shortcut.isEmpty()) {
            labelText = name;
        } else {
            labelText = name + " (" + shortcut + ")";
        }
        Label label = new Label("buttonLabel", labelText);
        label.add(new AttributeModifier("style", "font-weight: 400;"));

        button.add(label.setOutputMarkupId(true));
        shortcutBehavior.addShortcut(shortcut, "$(\"#" + id + "\").click();");
        return button;
    }

    /* Create input field to handle the product/category selected */
    private void initProductInput() {
        productInput = new TextField("product");
        productInput.add(new FocusOnLoadBehavior());
        productInput.add(new AttributeModifier("style", "height: 1px; padding: 0; width: 150px;"));
        form.add(productInput.setOutputMarkupId(true));
    }


    /* Table containing all products & categories of this order */
    private void initRowItems() {
        items = new WebMarkupContainer("items");

        ListView itemsListView = new ListView<RowItem>("productRow", model.getObject().getItems()) {
            @Override
            protected void populateItem(final ListItem<RowItem> item) {
                final RowItem row = item.getModel().getObject();
                item.add(new Label("productCount", new PropertyModel(item.getModel(), "amount")));
                item.add(new Label("productName", new PropertyModel(item.getModel(), "name")));
                item.add(new CurrencyLabel("productPrice", new PropertyModel(item.getModel(), "price")));
                item.add(new CurrencyLabel("productTotal", new PropertyModel(item.getModel(), "total")));
                Form itemForm = new Form("itemsForm");
                itemForm.add(new AjaxButton("productRemove", itemForm) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        super.onSubmit(target, form);
                        model.getObject().remove(item.getModel().getObject().getId());
                        target.add(container);
                    }
                }.setOutputMarkupPlaceholderTag(true));
                item.add(itemForm.setOutputMarkupPlaceholderTag(true));
                WebMarkupContainer truckParent = new WebMarkupContainer("truckParent");
                Form truckForm = new Form("truckForm");
                truckForm.setEnabled(BigDecimalHelper.isPositiveAmount(item.getModel().getObject().getPrice()));
                truckForm.setVisible(BigDecimalHelper.isPositiveAmount(item.getModel().getObject().getPrice()));
                truckForm.add(new AjaxButton("productTruck", truckForm) {
                    @Override
                    public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        super.onSubmit(target, form);
                        item.getModel().getObject().setTruck(!row.isTruck());
                        target.add(container);
                    }
                }.setOutputMarkupId(true));
                if (row.isTruck()) {
                    item.add(new AttributeModifier("style", "background-color: #bbb;"));
                } else {
                    truckParent.add(new AttributeModifier("style", "padding: 4px; text-align: center;"));
                }
                truckParent.add(truckForm.setOutputMarkupId(true));
                item.add(truckParent.setOutputMarkupPlaceholderTag(true));
                item.setVisible(item.getModel().getObject().getAmount() > 0);
            }
        };
        items.add(itemsListView.setOutputMarkupId(true));
        form.add(items.setOutputMarkupId(true));
    }

    /* Create total button*/
    private void initTotal() {
        AjaxButton totalButton = new AjaxButton("totalButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                if (BigDecimalHelper.isNotZero(model.getObject().getTotal())) {
                    target.prependJavaScript(Modal.showAndFocus(totalAmountModal, totalAmountField));
                    if (BigDecimalHelper.isPositiveAmount(model.getObject().getTotal())) {
                        totalInputAmount = null;
                        returnAmount = null;
                        payButton.setVisible(false);
                    } else {
                        totalInputAmount = BigDecimal.ZERO;
                        returnAmount = totalInputAmount.subtract(model.getObject().getTotal());
                        payButton.setVisible(true);
                    }
                    target.add(totalAmountForm, container);
                } else {
                    target.appendJavaScript("$(\"#product\").focus();");
                }
            }
        };
        shortcutBehavior.addShortcut("Ctrl+b", "$(\"#" + totalButton.getId() + "\").click();");
        totalButton.add(new CurrencyLabel("totalLabel", new PropertyModel(model, "total")));
//        totalForm.add(totalButton.setOutputMarkupPlaceholderTag(true));
//        container.add(totalForm.setOutputMarkupId(true));
        form.add(totalButton.setOutputMarkupPlaceholderTag(true));
        container.add(form.setOutputMarkupId(true));
    }

    /* Create amount modal */
    private void initAmountModal() {
        categoryAmountModal = new WebMarkupContainer("categoryAmountModal");
        categoryAmountModal.add(new ReFocusBehavior(categoryAmountField));
        final Form categoryForm = new Form("category-form");
        categoryForm.add(new CustomFeedbackPanel("categoryFeedback", new ContainerFeedbackMessageFilter(categoryForm)));
        categoryAmountField = new NumberTextField<>("categoryAmount", new PropertyModel(this, "categoryAmount"));
        categoryAmountField.add(PlaceholderBehavior.withText("0,0"));
        categoryForm.add(categoryAmountField.setOutputMarkupPlaceholderTag(true));
        AjaxButton addCategoryButton = new AjaxButton("addCategory", categoryForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                String input = model.getObject().getProduct().replace("\n", "");
                if (ProductHelper.isCategory(input)) {
                    Category category = categoryService.findCategory(input);
                    if (category != null && categoryAmount != null && BigDecimalHelper.isNotZero(categoryAmount)) {
                        model.getObject().add(category, categoryAmount);
                        model.getObject().setProduct(null);
                        productInput.clearInput();
                        productInput.setModel(Model.of(""));
                        categoryAmount = null;
                        categoryAmountField.clearInput();
                        target.add(container, categoryAmountModal);
                        target.appendJavaScript(Modal.hide(categoryAmountModal));
                    } else if (category == null) {
                        categoryForm.error(getString("error.product_not_found"));
                        target.add(categoryForm);
                    } else {
                        categoryForm.error(getString("error.invalid_amount"));
                        target.add(categoryForm);
                    }
                } else {
                    form.error(getString("error.product_not_found"));
                }
            }
        };
        AjaxButton closeCategoryButton = new AjaxButton("closeCategory", categoryForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                model.getObject().setProduct(null);
                productInput.clearInput();
                productInput.setModel(Model.of(""));
                categoryAmount = null;
                categoryAmountField.clearInput();
                target.add(container, categoryAmountModal);
                target.appendJavaScript(Modal.hide(categoryAmountModal));
            }
        };
        categoryForm.add(addCategoryButton.setOutputMarkupId(true));
        categoryForm.add(closeCategoryButton.setOutputMarkupId(true));
        categoryAmountModal.add(categoryForm.setOutputMarkupId(true));
        add(categoryAmountModal.setOutputMarkupPlaceholderTag(true));
    }

    /* Create total modal */
    private void initTotalModal() {
        totalAmountModal = new WebMarkupContainer("totalAmountModal");
        totalAmountModal.add(new ReFocusBehavior(totalAmountField));
        totalAmountForm = new Form("total-form");
        final CustomFeedbackPanel feedback = new CustomFeedbackPanel("totalFeedback", new ContainerFeedbackMessageFilter(totalAmountForm));
        totalAmountForm.add(feedback.setOutputMarkupPlaceholderTag(true));
        final CurrencyLabel totalLabel = new CurrencyLabel("totalAmount", new PropertyModel(model, "total"));
        totalAmountForm.add(totalLabel.setOutputMarkupId(true));

        final CurrencyLabel returnAmountLabel = new CurrencyLabel("returnAmount", new PropertyModel(this, "returnAmount"));
        totalAmountForm.add(returnAmountLabel.setOutputMarkupId(true));

        totalAmountField = new NumberTextField<BigDecimal>("totalAmountInput", new PropertyModel(this, "totalInputAmount")) {
            @Override
            public void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("placeholder", "0,0");
            }
        };
        totalAmountField.add(new AjaxEventBehavior("input") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                final BigDecimal input = BigDecimalHelper.asBigDecimal(totalAmountField.getInput());
                if (input == null) {
                    totalAmountForm.error(getString("error.invalid_amount"));
                    payButton.setVisible(false);
                    target.add(feedback, payButton, totalLabel);
                } else {
                    returnAmount = input.subtract(model.getObject().getTotal());
                    payButton.setVisible(returnAmount.compareTo(BigDecimal.ZERO) >= 0);
                    returnAmount = returnAmount.max(BigDecimal.ZERO);
                    target.add(returnAmountLabel, feedback, payButton, totalLabel);
                }
            }
        });

        totalAmountForm.add(totalAmountField.setOutputMarkupPlaceholderTag(true));

        payButton = new AjaxButton("finishTransaction", totalAmountForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                try {
                    Receipt receipt = transactionService.createTransaction(
                            getRegisterSession().getUser(),
                            model.getObject().getTotal(),
                            BigDecimalHelper.asBigDecimal(totalAmountField.getInput()),
                            returnAmount,
                            model.getObject().getItems());
                    clearPage();
                    target.appendJavaScript(Modal.hide(totalAmountModal));
//                    callPrinter(receipt);
                    setResponsePage(KassaPage.class);
                } catch (Exception e) {
                    totalAmountForm.error(getString("error.unknown"));
                }
            }
        };
        payButton.setVisible(false);
        AjaxButton closeTotalButton = new AjaxButton("closeTotal", totalAmountForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                model.getObject().setProduct(null);
                target.add(container, totalAmountModal);
                target.appendJavaScript(Modal.hide(totalAmountModal));
            }
        };
        totalAmountForm.add(payButton.setOutputMarkupPlaceholderTag(true));
        totalAmountForm.add(closeTotalButton.setOutputMarkupId(true));
        totalAmountModal.add(totalAmountForm.setOutputMarkupId(true));
        add(totalAmountModal.setOutputMarkupPlaceholderTag(true));
    }

    /* Reset entire page */
    private void clearPage() {
        model.getObject().clear();
        productInput.clearInput();
        categoryAmountField.clearInput();
        totalAmountField.clearInput();
        categoryAmount = BigDecimal.ZERO;
        totalInputAmount = BigDecimal.ZERO;
        returnAmount = BigDecimal.ZERO;
    }

    private void callPrinter(final Receipt receipt) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                WebRequest req = (WebRequest) RequestCycle.get().getRequest();
                HttpServletRequest httpReq = (HttpServletRequest) req.getContainerRequest();
                String hostName = httpReq.getRemoteHost();
                int portNumber = 4000;

                try {
                    InetAddress address = InetAddress.getByName(hostName);
                    Socket socket = new Socket(address, portNumber);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(receipt);
                    out.flush();
                    out.writeByte(-1);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    /* New product modal */

    private void initProductModal() {
        productForm = new Form("product-form",new CompoundPropertyModel(productModel));
        productModalFeedback = new CustomFeedbackPanel("productFeedback");
        productForm.add(productModalFeedback.setOutputMarkupPlaceholderTag(true));

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
                    model.getObject().add(product);
                    productInput.clearInput();
                    productInput.setModel(Model.of(""));

                    productsModel.getObject().resetIds();
                    productModel.setObject(new Product());
                    target.add(container, productModal);
                    target.appendJavaScript(Modal.hide(productModal));
                    showProductModal = false;
                } else {
                    target.add(productModalFeedback);
                }
            }

            @Override
            public void close(AjaxRequestTarget target) {
                productsModel.getObject().resetIds();
                target.add(container, productModal);
                target.appendJavaScript(Modal.hide(productModal));
                showProductModal = false;
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
}
