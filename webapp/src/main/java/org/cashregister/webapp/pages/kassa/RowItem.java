package org.cashregister.webapp.pages.kassa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.cashregister.domain.Category;
import org.cashregister.domain.Product;

/**
 * Created by derkhumblet on 29/11/14.
 */
public class RowItem implements Serializable {
    private String id;
    private String name;
    private BigDecimal price;
    private BigDecimal total;
    private Integer amount;
    private final boolean isProduct;
    private boolean truck;

    public RowItem(Product product) {
        this.id = product.getId().toString();
        this.name = product.getName();
        this.price = product.getPrice();
        this.total = product.getPrice();
        this.amount = 1;
        this.isProduct = true;
        this.truck = false;
    }

    public RowItem(Category category, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.name = category.getName();
        this.price = price;
        this.total = price;
        this.amount = 1;
        this.isProduct = false;
        this.truck = false;
    }

    public void add() {
        ++amount;
        calculateTotal();
    }

    public void reset() {
        amount = 0;
        total = BigDecimal.ZERO;
    }

    public void subtract() {
        --amount;
        calculateTotal();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    private void calculateTotal() {
        this.total = price.multiply(new BigDecimal(amount));
    }

    public boolean isProduct() {
        return isProduct;
    }

    public boolean isTruck() {
        return truck;
    }

    public void setTruck(boolean truck) {
        this.truck = truck;
    }
}
