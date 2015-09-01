package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Category;
import org.cashregister.webapp.persistence.api.CategoryRepository;
import org.cashregister.webapp.service.api.CategoryService;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derkhumblet on 16/11/14.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repo;

    @Override @Transactional
    public List<Category> getCategories(long merchantId) {
        try {
            return repo.findCategories(merchantId);
        } catch (TransactionException e) {
            repo.getEntityManager().close();
            return new ArrayList<>();
        }
    }

    @Override @Transactional
    public Category findCategory(String name, long merchantId) {
        try {
            return repo.findCategory(name, merchantId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override @Transactional
    public void delete(Category category) {
        try {
            repo.removeCategory(category);
        } catch (Exception e) {
            repo.getEntityManager().close();
            e.printStackTrace();
        }
    }

    @Override
    public Category readCategory(long id) {
        return repo.find(id);
    }

}
