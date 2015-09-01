package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.Category;
import org.cashregister.webapp.persistence.api.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by derkhumblet on 15/11/14.
 */
@Repository
public class JpaCategoryRepository extends JpaGenericRepository<Category> implements CategoryRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JpaCategoryRepository.class);
    private static final int ID_NAME = 1;

    @Override @Transactional
    public Category findCategory(String name, long merchantId) {
        TypedQuery<Category> q = getEntityManager().createQuery("SELECT c FROM Category c WHERE merchant.id = ?1 AND name = ?2", Category.class);
        q.setParameter(1, merchantId);
        q.setParameter(2, name);
        return q.getSingleResult();
    }

    @Override @Transactional
    public List<Category> findCategories(long merchantId) {
        TypedQuery<Category> q = getEntityManager().createQuery("SELECT c FROM Category c WHERE merchant.id = ?1", Category.class);
        q.setParameter(1, merchantId);
        return q.getResultList();
    }

    @Override @Transactional
    public void removeCategory(Category category) {
        getEntityManager()
                .createQuery("DELETE FROM Category c WHERE c.id = ?1")
                .setParameter(1, category.getId())
                .executeUpdate();
    }
}
