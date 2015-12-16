package org.cashregister.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by derkhumblet on 26/11/15.
 */
@Entity
@Table(name = "config")
public class Config extends GenericEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name="merchant")
    private Merchant merchant;

    @Column(name = "config_key")
    private String key;

    @Column(name = "config_value")
    private String value;

    public Config() { }

    public Config(Merchant merchant, String key, String value) {
        this.merchant = merchant;
        this.key = key;
        this.value = value;
    }

    public String value() {
        return value;
    }

    public void value(String value) { this.value = value; }
}
