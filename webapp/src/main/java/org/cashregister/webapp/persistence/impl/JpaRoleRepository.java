package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.Role;
import org.cashregister.webapp.persistence.api.RoleRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Repository
public class JpaRoleRepository extends JpaGenericRepository<Role> implements RoleRepository {

    @Override
    public void createRole(Role role) {
        getEntityManager().persist(role);
        getEntityManager().flush();
    }
}
