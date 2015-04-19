package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.User;
import org.cashregister.webapp.persistence.api.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Repository
public class JpaUserRepository extends JpaGenericRepository<User> implements UserRepository {

    @Override @Transactional
    public void createUser(User user) {
        getEntityManager().persist(user);
        getEntityManager().flush();
    }

    @Override
    public User findByName(String name) {
        TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.login = :name", User.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
