//package org.cashregister.webapp.pages.configuration;
//
//import org.apache.wicket.markup.repeater.data.IDataProvider;
//import org.apache.wicket.model.IModel;
//import org.apache.wicket.model.Model;
//import org.apache.wicket.spring.injection.annot.SpringBean;
//import org.cashregister.domain.Category;
//import org.cashregister.domain.Product;
//import org.cashregister.webapp.pages.product.ProductsModel;
//import org.cashregister.webapp.persistence.api.CategoryRepository;
//import org.cashregister.webapp.persistence.api.ProductRepository;
//import org.cashregister.webapp.util.ProductHelper;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//
///**
// * Created by derkhumblet on 12/12/14.
// */
//public class CategoriesProvider implements IDataProvider<Category> {
//    private CategoryRepository repo;
//    private IModel<CategoriesModel> model;
//    private final long merchantId;
//
//    public CategoriesProvider(CategoryRepository repo, IModel<CategoriesModel> model, long merchantId) {
//        this.repo = repo;
//        this.model = model;
//        this.merchantId = merchantId;
//    }
//
//    @Override
//    public long size() {
//        return repo.findCategories(merchantId).size();
//    }
//
//    @Override
//    public Iterator<Category> iterator(long first, long count) {
//        return repo.findCategories(merchantId/*, Long.valueOf(first).intValue(), Long.valueOf(count).intValue()*/).iterator();
//    }
//
//    @Override
//    public IModel<Category> model(Category category) {
//        return Model.of(category);
//    }
//
//    @Override
//    public void detach() { }
//}
