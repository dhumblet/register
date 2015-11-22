package org.cashregister.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by derkhumblet on 10/11/14.
 */
@Entity
@Table(name = "transaction_detail")
public class TransactionDetail extends GenericEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product")
    private Product product;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "truck", nullable = false)
    private boolean truck;

    @Column(name = "name")
    private String name;

    private TransactionDetail() { }

    public TransactionDetail(Category category, Transaction transaction, Product product, Integer amount, BigDecimal price, boolean truck) {
        this.category = category;
        this.transaction = transaction;
        this.product = product;
        this.amount = amount;
        this.price = price;
        this.truck = truck;
        this.name = product != null ? product.getName() : category.getName();
    }

    public Integer amount() {
        return amount;
    }

    public void amount(Integer amount) {
        this.amount = amount;
    }

    public Transaction transaction() {
        return transaction;
    }

    public void transaction(Transaction transaction) {
        this.transaction = transaction;
    }


    public BigDecimal price() {
        return price;
    }

    public void price(BigDecimal price) {
        this.price = price;
    }

    public boolean isTruck() {
        return truck;
    }

    public void truck(boolean truck) {
        this.truck = truck;
    }

    public String name() {
        return name;
    }


}
