package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.Category;

import java.util.List;

/**
 * Created by derkhumblet on 15/11/14.
 */
public interface CategoryRepository extends GenericRepository<Category> {

    Category findCategory(String name, long merchantId);

    List<Category> findCategories(long merchantId);

    void removeCategory(Category category);
}
