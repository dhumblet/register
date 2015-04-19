package org.cashregister.webapp.security.api;

import org.cashregister.domain.Merchant;
import org.cashregister.domain.User;
import org.cashregister.webapp.security.impl.SecurityDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by derkhumblet on 16/01/15.
 */
public interface SecurityDetailsService extends UserDetailsService {

    SecurityDetails getCurrentUser();

    @Override
    SecurityDetails loadUserByUsername(String username);

    void removeCurrentUser();

    void setCurrentUser(User user, String... requiredRights);

    void setCurrentUser(User user, Merchant merchant, String... requiredRights);
}
