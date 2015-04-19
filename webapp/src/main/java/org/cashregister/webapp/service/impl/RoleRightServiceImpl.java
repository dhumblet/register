package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Right;
import org.cashregister.domain.Role;
import org.cashregister.webapp.persistence.api.RightRepository;
import org.cashregister.webapp.persistence.api.RoleRepository;
import org.cashregister.webapp.service.api.RoleRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Service
public class RoleRightServiceImpl implements RoleRightService {
    @Autowired private RightRepository rightRepository;
    @Autowired private RoleRepository roleRepository;

    @Override
    public Right createRight(String name) {
        Right right = new Right(name);
        rightRepository.createRight(right);
        return right;
    }

    @Override
    public Role createRole(String name, Right... rights) {
        Role role;
        if (rights == null) {
            role = new Role(name);
        } else {
            role = new Role(name, new HashSet<Right>(Arrays.asList(rights)));
        }
        roleRepository.createRole(role);
        return role;
    }
}
