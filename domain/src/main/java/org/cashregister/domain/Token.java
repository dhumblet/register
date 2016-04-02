package org.cashregister.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by derkhumblet on 08/03/16.
 */
@Entity
@Table(name = "token")
public class Token extends GenericEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name="merchantId")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "clientId")
    private String clientId;

    @Column(name = "clientType")
    private String clientType;

    @Column(name = "created")
    private Date created;

    public Token() { }

    public Token(Merchant merchant, User user, String token, String clientId, String clientType) {
        this.merchant = merchant;
        this.user = user;
        this.token = token;
        this.clientId = clientId;
        this.clientType = clientType;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}