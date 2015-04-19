package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.Product;

import java.util.List;

/**
 * Created by derkhumblet on 15/11/14.
 */
public interface ProductRepository extends GenericRepository<Product> {

    Product findProduct(String serial, long merchantId);

    Product findProduct(Long id);

    List<Product> findProducts(String name, long merchantId, int first, int count);

    long countProducts(String name, long merchantId);

    void createProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Product product);
}
