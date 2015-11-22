package org.cashregister.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by derkhumblet on 02/03/15.
 */
public class OverviewLine implements Serializable {
    private String product, category, name;
    private BigDecimal amount, price;

    private OverviewLine() { }

    public OverviewLine(Object[] properties) {
        int col = 0;
        name = (String) properties[col++];
        product = (String) properties[col++];
        category = (String) properties[col++];
        amount = (BigDecimal) properties[col++];
        price = (BigDecimal) properties[col++];
    }

    /* Get */

    public String name() {
        return product == null && name.equalsIgnoreCase(category) ? "/" : name;
    }

    public String category() {
        return category == null ? name : category;
    }

    public BigDecimal amount() {
        return amount;
    }

    public BigDecimal price() {
        return price;
    }

}
