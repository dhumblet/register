package org.cashregister.webapp.password.api;

import org.cashregister.password.PasswordDetails;
import org.cashregister.webapp.error.PasswordException;
import org.cashregister.webapp.security.impl.SecurityDetails;

/**
 * Created by derkhumblet on 12/01/15.
 */
public interface PasswordService {

    void createPassword(PasswordDetails passwordDetails, String password);

    void changePassword(PasswordDetails passwordDetails, String oldPassword, String newPassword) throws PasswordException;

    void checkPassword(PasswordDetails passwordDetails, String password) throws PasswordException;

    void checkPassword(SecurityDetails securityDetails, String password) throws PasswordException;

    void checkHashedPassword(PasswordDetails passwordDetails, String password) throws PasswordException;

}
