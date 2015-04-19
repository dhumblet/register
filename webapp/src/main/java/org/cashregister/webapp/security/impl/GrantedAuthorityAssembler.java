package org.cashregister.webapp.security.impl;

import org.cashregister.domain.Right;
import org.cashregister.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by derkhumblet on 16/01/15.
 */
public final class GrantedAuthorityAssembler {
    private GrantedAuthorityAssembler() { }

    public static List<GrantedAuthority> build(Role role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Set<Right> rights = role.getRights();
        for (Right right : rights) {
            grantedAuthorities.add(new SimpleGrantedAuthority(right.getName()));
        }

        return grantedAuthorities;
    }
}
