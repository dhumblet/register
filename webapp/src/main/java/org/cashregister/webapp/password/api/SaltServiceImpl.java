package org.cashregister.webapp.password.api;

import org.cashregister.password.PasswordDetails;
import org.cashregister.webapp.error.PasswordError;
import org.cashregister.webapp.error.PasswordException;
import org.cashregister.webapp.security.impl.SecurityDetails;
import org.springframework.stereotype.Service;

/**
 * Created by derkhumblet on 12/01/15.
 */
@Service
public class SaltServiceImpl implements SaltService {

    @Override
    public String generateSaltById(PasswordDetails passwordDetails) {
        if (passwordDetails == null || passwordDetails.getPasswordDetailsId() == null) {
            throw new PasswordException(PasswordError.NO_ID_FOR_SALT);
        }
        return String.valueOf(passwordDetails.getPasswordDetailsId());
    }

    @Override
    public String generateSaltById(SecurityDetails details) {
        if (details == null) {
            throw new PasswordException(PasswordError.NO_ID_FOR_SALT);
        }
        return String.valueOf(details.getUserId());
    }
}
