package org.cashregister.domain;

import javax.jws.soap.SOAPBinding;
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

    @Column(name = "paymentCost", nullable = false)
    private BigDecimal paymentCost;

    @ManyToOne
    @JoinColumn(name="merchantId")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @Column(name = "truck", nullable = false)
    private boolean truck;

    public Transaction() { }

    public Date date() {
        return date;
    }

    public BigDecimal price() {
        return price;
    }

    public BigDecimal totalPrice() { return price.add(paymentCost); }

    public Payment payment() {
        return payment;
    }

    public BigDecimal received() {
        return received;
    }

    public BigDecimal returned() {
        return returned;
    }

    public Merchant merchant() {
        return merchant;
    }

    public User user() {
        return user;
    }

    public boolean isTruck() {
        return truck;
    }


    public BigDecimal paymentCost() {
        return this.paymentCost;
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


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Transaction instance = new Transaction();

        public Builder price(BigDecimal price) {
            instance.price = price;
            return this;
        }

        public Builder received(BigDecimal received) {
            instance.received = received;
            return this;
        }

        public Builder payment(Payment payment) {
            instance.payment = payment;
            return this;
        }

        public Builder returned(BigDecimal returned) {
            instance.returned = returned;
            return this;
        }

        public Builder truck(boolean truck) {
            instance.truck = truck;
            return this;
        }

        public Builder paymentCost(BigDecimal paymentCost) {
            instance.paymentCost = paymentCost;
            return this;
        }

        public Builder user(User user) {
            instance.user = user;
            if (user.getMerchant() != null) {
                instance.merchant = user.getMerchant();
            }
            return this;
        }

        public Transaction build() {
            Transaction result = instance;
            instance = new Transaction();
            result.date = new Date();
            return result;
        }
    }

}
