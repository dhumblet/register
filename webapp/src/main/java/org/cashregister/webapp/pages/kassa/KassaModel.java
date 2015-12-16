package org.cashregister.webapp.pages.kassa;

import org.cashregister.domain.Category;
import org.cashregister.domain.Product;
import org.cashregister.webapp.service.api.ConfigService;

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
    private BigDecimal paymentCost = BigDecimal.ZERO;

    public KassaModel() { }

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
        removePaymentCost();
    }


    public void add(Category category, BigDecimal price) {
        removePaymentCost();
        items.add(new RowItem(category, price));
    }

    public void add(Product product) {
        removePaymentCost();
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
        return total.add(paymentCost);
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
        removePaymentCost();
        product = "";
        items = new ArrayList<>();
    }

    /* Payment cost */

    public void checkPaymentCost(ConfigService configService) {
        removePaymentCost();
        if (getTotal().compareTo(configService.electronicPaymentLimit()) < 0) {
            paymentCost = configService.electronicPaymentCost();
        }
    }

    public void removePaymentCost() {
        paymentCost = BigDecimal.ZERO;
    }
}
