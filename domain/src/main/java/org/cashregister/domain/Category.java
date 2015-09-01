package org.cashregister.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by derkhumblet on 10/11/14.
 */
@Entity
@Table(name = "category")
public class Category extends GenericEntity implements Serializable {

    private String name;
    private String shortcut;

    @ManyToOne
    @JoinColumn(name="merchantId")
    private Merchant merchant;

    public Category() { }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
