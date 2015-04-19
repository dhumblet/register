package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Product;
import org.cashregister.webapp.persistence.api.ProductRepository;
import org.cashregister.webapp.service.api.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

/**
 * Created by derkhumblet on 16/11/14.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repo;

    @Override @Transactional
    public Product findProduct(String serial, long merchantId) {
        return repo.findProduct(serial, merchantId);
    }

    @Override @Transactional
    public Product findProduct(String serial, long merchantId, boolean shorterFallback) {
        Product result = null;
        try {
            result = findProduct(serial, merchantId);
        } catch (NoResultException e) {
            result = null;
        }
        return result == null && shorterFallback ? findProduct(serial.substring(0, serial.length() - 3), merchantId) : result;
    }

    @Override
    public Product readProduct(long id) {
        return repo.find(id);
    }

    @Override
    public void update(Product product) {
        repo.updateProduct(product);
    }

    @Override
    public void create(Product product) {
        repo.createProduct(product);
    }

    @Override
    public void delete(Product product) {
        repo.deleteProduct(product);
    }
}
