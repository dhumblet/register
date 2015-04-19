package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.Category;
import org.cashregister.webapp.persistence.api.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by derkhumblet on 15/11/14.
 */
@Repository
public class JpaCategoryRepository extends JpaGenericRepository<Category> implements CategoryRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JpaCategoryRepository.class);
    private static final int ID_NAME = 1;

    @Override
    public Category findCategory(String name) {
        TypedQuery<Category> q = getEntityManager().createQuery("SELECT c FROM Category c WHERE name = ?1", Category.class);
        q.setParameter(1, name);
        return q.getSingleResult();
    }

    @Override
    public List<Category> findCategories(long merchantId) {
        //TODO dhumblet: add merchantId to category search
        TypedQuery<Category> q = getEntityManager().createQuery("SELECT c FROM Category c", Category.class);
        return q.getResultList();
    }

}
