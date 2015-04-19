package org.cashregister.domain;

import org.cashregister.password.PasswordDetails;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Entity
@Table(name="user")
public class User extends GenericEntity implements PasswordDetails, Serializable {
    private String login;
    private String password;
    @ManyToOne
    @JoinColumn(name="merchantId")
    private Merchant merchant;
    private Boolean locked;
    @ManyToOne
    @JoinColumn(name="roleId")
    private Role role;

    public User() {}

    public User(String login, Merchant merchant, Role role) {
        this.login = login;
        this.merchant = merchant;
        this.role = role;
        this.locked = false;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /* PasswordDetails */

    @Override
    public Long getPasswordDetailsId() {
        return getId();
    }

    @Override
    public String getPasswordHashed() {
        return getPassword();
    }

    @Override
    public void setPasswordHashed(String passwordHashed) {
        setPassword(passwordHashed);
    }

    @Override
    public void lock() {
        setLocked(true);
    }

    @Override
    public void unlock() {
        setLocked(false);
    }

    @Override
    public boolean isLocked() {
        return getLocked();
    }

    public boolean hasRight(String name) {
        for (Right right : role.getRights()) {
            if (right.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
