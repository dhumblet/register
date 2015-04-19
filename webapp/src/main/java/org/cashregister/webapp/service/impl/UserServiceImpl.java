package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Merchant;
import org.cashregister.domain.Role;
import org.cashregister.domain.User;
import org.cashregister.webapp.password.api.PasswordService;
import org.cashregister.webapp.persistence.api.UserRepository;
import org.cashregister.webapp.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordService passwordService;

    @Override
    public User createUser(String login, String password, Merchant merchant, Role role) {
        User user = new User(login, merchant, role);
        userRepository.createUser(user);
        passwordService.createPassword(user, password);
        userRepository.merge(user);
        return user;
    }

    @Override @Transactional
    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}
