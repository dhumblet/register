package org.cashregister.webapp.login;

import org.cashregister.webapp.password.api.PasswordService;
import org.cashregister.webapp.security.api.SecurityDetailsService;
import org.cashregister.webapp.security.impl.SecurityDetails;
import org.cashregister.webapp.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by derkhumblet on 16/01/15.
 */
@Component("authenticationProvider")
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired private SecurityDetailsService securityDetailsService;
    @Autowired private UserService userService;
    @Autowired private PasswordService passwordService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        SecurityDetails details = (SecurityDetails) userDetails;
        checkPassword(details, authentication);
    }

    @Transactional
    public void checkPassword(SecurityDetails details, UsernamePasswordAuthenticationToken authentication) {
        passwordService.checkPassword(details, String.valueOf(authentication.getCredentials()));
    }

    @Transactional(propagation = Propagation.NEVER)
    @Override
    protected final SecurityDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return doRetrieveUser(authentication);
    }

    @Transactional
    private SecurityDetails doRetrieveUser(UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        SecurityDetails details = securityDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if (details.getMerchantId() == null) {
            throw new AuthenticationServiceException("Geen handelaar geconfigureerd voor deze gebruiker.");
        }
        return details;
    }
}
