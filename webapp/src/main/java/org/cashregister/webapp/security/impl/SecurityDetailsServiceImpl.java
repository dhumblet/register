package org.cashregister.webapp.security.impl;

import org.cashregister.domain.Merchant;
import org.cashregister.domain.User;
import org.cashregister.webapp.security.api.SecurityDetailsService;
import org.cashregister.webapp.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Created by derkhumblet on 16/01/15.
 */
@Service
@Transactional
public class SecurityDetailsServiceImpl implements SecurityDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityDetailsServiceImpl.class);

    @Autowired private UserService userService;

    @Override
    public void removeCurrentUser() {
        if (getCurrentUser() == null) {
            LOG.warn("The current user is no set");
            return;
        }

        if (getCurrentUser().getCreatorId() != Thread.currentThread().getId()) {
            throw new IllegalStateException("The current user is set by someone else");
        }

        SecurityContextHolder.clearContext();
    }

    @Transactional
    @Override
    public void setCurrentUser(User user, String... requiredRights) {
        validateCurrentUserNotSet();
        validateUser(user, requiredRights);

        SecurityDetails details = SecurityDetailsAssembler.build(user);
        setContext(details);
    }

    @Transactional
    @Override
    public void setCurrentUser(User user, Merchant merchant, String... requiredRights) {
        Assert.notNull(merchant, "merchant doesn't exist");
        Assert.notEmpty(requiredRights, "missing required rights");

        setCurrentUser(user, requiredRights);
    }

    private void validateCurrentUserNotSet() {
        if (getCurrentUser() != null) {
            throw new IllegalStateException("The current user is already set");
        }
    }

    private void validateUser(User user, String... requiredRights) {
        Assert.notEmpty(requiredRights, "missing required rights");

        boolean hasAllRights = true;
        for (String right : requiredRights) {
            if (!user.hasRight(right)) {
                hasAllRights = false;
                break;
            }
        }

        Assert.isTrue(hasAllRights, "user doesn't have all required rights");
    }

    private void setContext(SecurityDetails details) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Override
    public SecurityDetails getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return null;
        }

        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return null;
        }

        if (SecurityDetails.class.isAssignableFrom(principal.getClass())) {
            return (SecurityDetails) principal;
        }

        throw new IllegalStateException("Unknown security details type:" + principal.getClass());
    }

    @Transactional(noRollbackFor = UsernameNotFoundException.class)
    @Override
    public SecurityDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userService.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return SecurityDetailsAssembler.build(user);
    }
}
