package org.cashregister.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derkhumblet on 10/02/15.
 */
public class Receipt implements Serializable {
    private String merchant, total, recevied, returned;
    private List<ReceiptDetail> details = new ArrayList<>();

    public static Receipt forTransaction(Transaction transaction) {
        Receipt receipt = new Receipt();
        receipt.merchant = transaction.getMerchant().getName();
        receipt.total = transaction.getPrice().toString();
        receipt.recevied = transaction.getReceived().toString();
        receipt.returned = transaction.getReturned().toString();
        return receipt;
    }

    public void addDetail(TransactionDetail detail) {
        this.details.add(new ReceiptDetail(detail));
    }

    public String getMerchant() {
        return merchant;
    }

    public String getTotal() {
        return total;
    }

    public String getRecevied() {
        return recevied;
    }

    public String getReturned() {
        return returned;
    }

    public List<ReceiptDetail> getDetails() {
        return details;
    }

    public class ReceiptDetail implements Serializable {
        private String name, count, price;

        public ReceiptDetail(TransactionDetail detail) {
            this.name = detail.getProduct() != null ?
                    detail.getProduct().getName() :
                    detail.getCategory().getName();
            this.count = String.valueOf(detail.getAmount().intValue());
            this.price = detail.getPrice().toString();
        }

        public String getName() {
            return name;
        }

        public String getCount() {
            return count;
        }

        public String getPrice() {
            return price;
        }
    }
}
