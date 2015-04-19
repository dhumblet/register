package org.cashregister.webapp.service.api;

import org.cashregister.domain.Product;

/**
 * Created by derkhumblet on 16/11/14.
 */
public interface ProductService {
    /**
     * Search a product based on the serial
     */
    Product findProduct(String serial, long merchantId);

    /**
     * Search a product based on the serial
     */
    Product findProduct(String serial, long merchantId, boolean shorterFallback);

    /**
     * Find a product based on the id
     */
    Product readProduct(long id);

    void update(Product product);

    void create(Product product);

    void delete(Product product);
}
