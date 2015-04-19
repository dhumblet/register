package org.cashregister.webapp.service.api;

import org.cashregister.domain.Right;
import org.cashregister.domain.Role;

/**
 * Created by derkhumblet on 11/01/15.
 */
public interface RoleRightService {

    Right createRight(String name);

    Role createRole(String name, Right... rights);
}
