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

    public TransactionDetail() { }

    public TransactionDetail(Category category, Transaction transaction, Product product, Integer amount, BigDecimal price, boolean truck) {
        this.category = category;
        this.transaction = transaction;
        this.product = product;
        this.amount = amount;
        this.price = price;
        this.truck = truck;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Category getCategory() {

        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isTruck() {
        return truck;
    }

    public void setTruck(boolean truck) {
        this.truck = truck;
    }
}
