package org.cashregister.webapp.pages.kassa;

import org.cashregister.domain.Category;
import org.cashregister.domain.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derkhumblet on 24/11/14.
 */
public class KassaModel implements Serializable {
    private String product;
    private List<RowItem> items = new ArrayList<>();

    public KassaModel() {
        System.out.println();
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public List<RowItem> getItems() {
        return items;
    }

    public void resetItems() {
        this.items = new ArrayList<>();
    }


    public void add(Category category, BigDecimal price) {
        items.add(new RowItem(category, price));
    }

    public void add(Product product) {
        for (RowItem item : items) {
            if (item.getId().equals(product.getId().toString())) {
                items.get(items.indexOf(item)).add();
                return;
            }
        }
        items.add(new RowItem(product));
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (RowItem item : items) {
            total = total.add(item.getPrice().multiply(new BigDecimal(item.getAmount())));
        }
        return total;
    }

    public void remove(String id) {
        for (RowItem item : items) {
            if (item.getId().equals(id)) {
                items.get(items.indexOf(item)).reset();
                return;
            }
        }
    }

    public void clear() {
        product = "";
        items = new ArrayList<>();
    }
}
