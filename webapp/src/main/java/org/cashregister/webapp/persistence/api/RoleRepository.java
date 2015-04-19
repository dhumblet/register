package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.Role;

/**
 * Created by derkhumblet on 11/01/15.
 */
public interface RoleRepository extends GenericRepository<Role> {

    void createRole(Role role);
}
