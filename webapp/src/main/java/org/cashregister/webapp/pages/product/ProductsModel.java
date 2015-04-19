package org.cashregister.webapp.pages.product;

import java.io.Serializable;

/**
 * Created by derkhumblet on 12/12/14.
 */
public class ProductsModel implements Serializable {
    private String search = "";
    private Long editProductId;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Long getEditProductId() {
        return editProductId;
    }

    public void setEditProductId(Long editProductId) {
        this.editProductId = editProductId;
    }

    public void clear() {
        search = "";
        editProductId = null;
    }

    public void resetIds() {
        editProductId = null;
    }
}
