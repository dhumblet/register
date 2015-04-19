package org.cashregister.webapp.service.api;

import org.cashregister.domain.Merchant;
import org.cashregister.domain.Role;
import org.cashregister.domain.User;

/**
 * Created by derkhumblet on 11/01/15.
 */
public interface UserService {

    User createUser(String login, String password, Merchant merchant, Role role);

    User findByName(String name);
}
