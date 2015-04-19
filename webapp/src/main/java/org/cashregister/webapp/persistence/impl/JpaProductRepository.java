package org.cashregister.webapp.persistence.impl;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.cashregister.domain.Product;
import org.cashregister.webapp.persistence.api.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by derkhumblet on 15/11/14.
 */
@Repository
public class JpaProductRepository extends JpaGenericRepository<Product> implements ProductRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JpaProductRepository.class);

    @Override @Transactional
    public Product findProduct(String serial, long merchantId) {
        try {
            TypedQuery<Product> q = getEntityManager().createQuery("SELECT p FROM Product p WHERE serial = ?1 AND merchant.id = ?2", Product.class);
            q.setParameter(1, serial);
            q.setParameter(2, merchantId);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override @Transactional
    public Product findProduct(Long id) {
        TypedQuery<Product> q = getEntityManager().createQuery("SELECT p FROM Product p WHERE id = ?1", Product.class);
        q.setParameter(1, id);
        return q.getSingleResult();
    }

    @Override @Transactional
    public List<Product> findProducts(String name, long merchantId, int first, int count) {
        TypedQuery<Product> q = getEntityManager().createQuery("SELECT p FROM Product p WHERE name LIKE ?1 AND merchant.id = ?2 ORDER BY id DESC", Product.class);
        q.setParameter(1, "%" + name + "%");
        q.setParameter(2, merchantId);
        q.setFirstResult(first);
        q.setMaxResults(count);
        return q.getResultList();
    }

    @Override @Transactional
    public long countProducts(String name, long merchantId) {
        TypedQuery<Long> q = getEntityManager().createQuery("SELECT COUNT(p) FROM Product p WHERE name LIKE ?1 AND merchant.id = ?2", Long.class);
        q.setParameter(1, "%" + name + "%");
        q.setParameter(2, merchantId);
        return q.getSingleResult();
    }

    @Override @Transactional
    public void updateProduct(Product product) {
        Product persistedProduct = findProduct(product.getId());
        persistedProduct.setCategory(product.getCategory());
        persistedProduct.setName(product.getName());
        persistedProduct.setPrice(product.getPrice());
        persistedProduct.setSerial(product.getSerial());
        persistedProduct.setMerchant(product.getMerchant());
        getEntityManager().merge(persistedProduct);
        getEntityManager().flush();
    }

    @Override @Transactional
    public void createProduct(Product product) {
        getEntityManager().persist(product);
        getEntityManager().flush();
    }

    @Override @Transactional
    public void deleteProduct(Product product) {
        Query q = getEntityManager().createQuery("DELETE FROM Product p WHERE id = ?1");
        q.setParameter(1, product.getId());
        q.executeUpdate();
    }
}
