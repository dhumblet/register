package org.cashregister.webapp.password.api;

import org.cashregister.password.PasswordDetails;
import org.cashregister.webapp.security.impl.SecurityDetails;

/**
 * Created by derkhumblet on 12/01/15.
 */
public interface SaltService {

    String generateSaltById(PasswordDetails passwordDetails);

    String generateSaltById(SecurityDetails details);
}
