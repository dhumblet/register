package org.cashregister.webapp.security.impl;

import org.cashregister.domain.User;

import java.util.Locale;

/**
 * Created by derkhumblet on 16/01/15.
 */
public final class SecurityDetailsAssembler {

    private SecurityDetailsAssembler() { }

    public static SecurityDetails build(User user) {
        SecurityDetails userDetails = new SecurityDetails();

        userDetails.setUsername(user.getLogin());
        userDetails.setPassword(user.getPassword());
        userDetails.setAuthorities(GrantedAuthorityAssembler.build(user.getRole()));
        userDetails.setAccountNonLocked(!user.isLocked());
        userDetails.setAccountNonExpired(true);
        userDetails.setCredentialsNonExpired(true);
        userDetails.setEnabled(true);
        userDetails.setUserId(user.getId());
        userDetails.setLocale(new Locale("nl", "BE"));
        userDetails.setLocked(user.isLocked());
        if (user.getMerchant() != null) {
            userDetails.setMerchantId(user.getMerchant().getId());
            userDetails.setMerchantName(user.getMerchant().getName());
        }

        return userDetails;
    }
}
