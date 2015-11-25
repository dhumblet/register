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
                @ColumnResult(name = "name"),
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

    public Transaction(BigDecimal price, BigDecimal received, Payment payment, BigDecimal returned, boolean truck) {
        this.date = new Date();
        this.price = price;
        this.received = received;
        this.returned = returned;
        this.payment = payment == null ? Payment.CASH : payment;
        this.truck = truck;
    }

    public Date date() {
        return date;
    }

    public BigDecimal price() {
        return price;
    }

    public void price(BigDecimal price) {
        this.price = price;
    }

    public Payment payment() {
        return payment;
    }

    public void payment(Payment payment) {
        this.payment = payment;
    }

    public BigDecimal received() {
        return received;
    }

    public BigDecimal returned() {
        return returned;
    }

    public void returned(BigDecimal returned) {
        this.returned = returned;
    }

    public Merchant merchant() {
        return merchant;
    }

    public void merchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public User user() {
        return user;
    }

    public void user(User user) {
        this.user = user;
    }

    public boolean isTruck() {
        return truck;
    }

    public void truck(boolean truck) {
        this.truck = truck;
    }

    public enum Payment implements Serializable {
        CASH(0, "Cash"), CARD(1, "Bancontact");

        private Integer id;
        private String representation;

        Payment(Integer id, String representation) {
            this.id = id; this.representation = representation;
        }

        public Integer id() {
            return id;
        }

        public String representation() { return representation; }
    }
}
