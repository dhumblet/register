package org.cashregister.webapp.pages.product;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.Product;
import org.cashregister.webapp.persistence.api.ProductRepository;
import org.cashregister.webapp.util.ProductHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by derkhumblet on 12/12/14.
 */
public class ProductProvider implements IDataProvider<Product> {
    @SpringBean private ProductRepository repo;
    private IModel<ProductsModel> model;
    private final long merchantId;

    public ProductProvider(ProductRepository repo, IModel<ProductsModel> model, long merchantId) {
        this.repo = repo;
        this.model = model;
        this.merchantId = merchantId;
    }

    @Override
    public long size() {
        return ProductHelper.isProduct(model.getObject().getSearch()) ? getSizeBySerial() : getSizeByName();
    }

    @Override
    public Iterator<Product> iterator(long first, long count) {
        String search = model.getObject().getSearch() == null ? "" : model.getObject().getSearch();
        return ProductHelper.isProduct(search)
                ? productAsList(repo.findProduct(search, merchantId))
                : repo.findProducts(search, merchantId, Long.valueOf(first).intValue(), Long.valueOf(count).intValue()).iterator();
    }

    private Iterator<Product> productAsList(Product product) {
        return product == null ? (new ArrayList()).iterator() : Arrays.asList(product).iterator();
    }

    private long getSizeBySerial() {
        return repo.findProduct(model.getObject().getSearch(), merchantId) == null ? 0 : 1;
    }

    private long getSizeByName() {
        String search = model.getObject().getSearch() == null ? "" : model.getObject().getSearch();
        return repo.countProducts(search, merchantId);
    }

    @Override
    public IModel<Product> model(Product product) {
        return Model.of(product);
    }

    @Override
    public void detach() { }
}
