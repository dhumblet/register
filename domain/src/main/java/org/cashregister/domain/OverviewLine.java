package org.cashregister.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by derkhumblet on 02/03/15.
 */
public class OverviewLine implements Serializable {
    private String product, category;
    private BigDecimal amount, price;

    public OverviewLine(String product, String category, BigDecimal amount, BigDecimal price) {
        this.product = product;
        this.category = category;
        this.amount = amount;
        this.price = price;
    }

    public OverviewLine(Object[] properties) {
        int col = 0;
        product = (String) properties[col++];
        product = product == null ? "/" : product;
        category = (String) properties[col++];
        amount = (BigDecimal) properties[col++];
        price = (BigDecimal) properties[col++];
    }

    public OverviewLine() { }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
