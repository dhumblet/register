package org.cashregister.webapp.pages.configuration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.Product;
import org.cashregister.webapp.component.Modal;
import org.cashregister.webapp.navigator.PagingNavigator;
import org.cashregister.webapp.pages.product.ProductPageCallback;
import org.cashregister.webapp.pages.product.ProductProvider;
import org.cashregister.webapp.pages.product.ProductsDataView;
import org.cashregister.webapp.pages.product.ProductsModel;
import org.cashregister.webapp.pages.template.SecureTemplatePage;
import org.cashregister.webapp.persistence.api.CategoryRepository;
import org.cashregister.webapp.persistence.api.ProductRepository;
import org.cashregister.webapp.service.api.CategoryService;

/**
 * Created by derkhumblet on 07/06/15.
 */
public class ConfigurationPage extends SecureTemplatePage {
    private WebMarkupContainer container;
    private CategoriesDataView categoriesDataView;
    private IModel<CategoriesModel> categoryModel;

    @SpringBean private CategoryRepository categoryRepo;
    @SpringBean private CategoryService categoryService;



    @Override
    public void onInitialize() {
        super.onInitialize();
        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupPlaceholderTag(true));

        categoryModel = Model.of(new CategoriesModel());
        initCategories();
    }

    private void initCategories() {
        WebMarkupContainer table = new WebMarkupContainer("categoriesTable");
        container.add(table.setOutputMarkupId(true));

        categoriesDataView = new CategoriesDataView("category", new CategoriesProvider(categoryRepo, categoryModel, getMerchantId()), categoryModel, container, categoryService, new ConfigurationPageCallback() {
            @Override
            public void edit(AjaxRequestTarget target) {
                Long editId = categoryModel.getObject().getEditCategoryId();
                if (editId != null) {
//                    categoryModel.setObject(product(editId));
                } else {
//                    categoryModel.setObject(null);
                }
                target.add(container);
            }
        });

        categoriesDataView.setItemsPerPage(100);
        table.add(categoriesDataView.setOutputMarkupId(true));

//        container.add(new PagingNavigator("pagerBottom", categoriesDataView).setAutoHide(true));
    }





    @Override
    public String getTitle() {
        return getString("menu.config");
    }
}
