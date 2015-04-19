package org.cashregister.domain;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by derkhumblet on 10/11/14.
 */
@SqlResultSetMapping(name = "Statistic",
        columns = {
                @ColumnResult(name = "product"),
                @ColumnResult(name = "category"),
                @ColumnResult(name = "amount"),
                @ColumnResult(name = "price")

        })
@Entity
@Table(name = "transaction")
public class Transaction extends GenericEntity implements Serializable{

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "received", nullable = false)
    private BigDecimal received;

    @Column(name = "returned", nullable = false)
    private BigDecimal returned;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "payment")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name="merchantId")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @Column(name = "truck", nullable = false)
    private boolean truck;

    public Transaction() { }

    public Transaction(BigDecimal price, Payment payment) {
        this.date = new Date();
        this.price = price;
        this.payment = payment;
    }

    public Transaction(BigDecimal price, BigDecimal received, BigDecimal returned, boolean truck) {
        this.date = new Date();
        this.price = price;
        this.received = received;
        this.returned = returned;
        this.payment = Payment.CASH;
        this.truck = truck;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public BigDecimal getReceived() {
        return received;
    }

    public void setReceived(BigDecimal received) {
        this.received = received;
    }

    public BigDecimal getReturned() {
        return returned;
    }

    public void setReturned(BigDecimal returned) {
        this.returned = returned;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isTruck() {
        return truck;
    }

    public void setTruck(boolean truck) {
        this.truck = truck;
    }

    public enum Payment implements Serializable {
        CASH(0), CARD(1);

        private Integer id;

        Payment(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }
    }
}
