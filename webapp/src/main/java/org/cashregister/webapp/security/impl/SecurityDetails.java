package org.cashregister.webapp.security.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Locale;

/**
 * Created by derkhumblet on 16/01/15.
 */
public class SecurityDetails implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private String password;
    private String username;
    private Locale locale = Locale.ENGLISH;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private long userId;
    private Long merchantId;
    private String merchantName;
    private boolean locked;

    private long creatorId;

    public SecurityDetails() {
        creatorId = Thread.currentThread().getId();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long value) {
        userId = value;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public boolean hasNotRight(String... rightConstants) {
        for (GrantedAuthority gra : authorities) {
            for (String rightConstant : rightConstants) {
                if (gra.getAuthority().equals(rightConstant)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return the id of the thread that created this details object
     */
    public long getCreatorId() {
        return creatorId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "SecurityDetails [userId=" + userId
                + ", username=" + username
                + ", enabled=" + enabled
                + ", accountNonLocked=" + accountNonLocked
                + ", accountNonExpired=" + accountNonExpired
                + ", credentialsNonExpired=" + credentialsNonExpired + "]";
    }
}
