package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.User;

/**
 * Created by derkhumblet on 11/01/15.
 */
public interface UserRepository extends GenericRepository<User> {

    void createUser(User user);

    User findByName(String name);
}
