package org.cashregister.webapp.service.api;

import org.cashregister.domain.Category;

import java.util.List;

/**
 * Created by derkhumblet on 16/11/14.
 */
public interface CategoryService {
    /**
     * @return list of all available categories.
     */
    List<Category> getCategories(long merchantId);

    /**
     * Search a category based on name
     */
    Category findCategory(String name, long merchantId);

    /**
     * Remove a given category
     * @param category
     */
    void delete(Category category);

    /**
     * Retrun a category based on id
     * @param id
     * @return
     */
    Category readCategory(long id);
}
