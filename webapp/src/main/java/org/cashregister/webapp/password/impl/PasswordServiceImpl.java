package org.cashregister.webapp.password.impl;

import org.cashregister.webapp.error.PasswordError;
import org.cashregister.webapp.error.PasswordException;
import org.cashregister.password.PasswordDetails;
import org.cashregister.webapp.password.api.PasswordService;
import org.cashregister.webapp.password.api.SaltService;
import org.cashregister.webapp.security.api.HashService;
import org.cashregister.webapp.security.impl.SecurityDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by derkhumblet on 12/01/15.
 */
@Service
public class PasswordServiceImpl implements PasswordService {
    @Autowired private HashService hashService;
    @Autowired private SaltService saltService;

    @Override
    public void createPassword(PasswordDetails passwordDetails, String password) {
        passwordDetails.setPasswordHashed(hashString(passwordDetails, password));
    }

    @Override
    public void changePassword(PasswordDetails passwordDetails, String oldPassword, String newPassword) throws PasswordException {
        if (passwordDetails.isLocked()) {
            throw new PasswordException(PasswordError.ENTITY_LOCKED);
        }
        if (matchPassword(passwordDetails, oldPassword)) {
            String newHashedPassword = hashString(passwordDetails, newPassword);
            passwordDetails.setPasswordHashed(newHashedPassword);
        } else {
            throw new PasswordException(PasswordError.PASSWORD_INCORRECT);
        }
    }

    @Override
    public void checkPassword(PasswordDetails passwordDetails, String password) throws PasswordException {
        if (passwordDetails.isLocked()) {
            throw new PasswordException(PasswordError.ENTITY_LOCKED);
        }
        if (!matchPassword(passwordDetails, password)) {
            throw new PasswordException(PasswordError.PASSWORD_INCORRECT);
        }
    }

    @Override
    public void checkPassword(SecurityDetails securityDetails, String password) throws PasswordException {
        if (securityDetails.isLocked()) {
            throw new PasswordException(PasswordError.ENTITY_LOCKED);
        }
        if (!matchPassword(securityDetails, password)) {
            throw new PasswordException(PasswordError.PASSWORD_INCORRECT);
        }
    }

    @Override
    public void checkHashedPassword(PasswordDetails passwordDetails, String password) throws PasswordException {
        if (passwordDetails.isLocked()) {
            throw new PasswordException(PasswordError.ENTITY_LOCKED);
        }
        if (!matchHashedPassword(passwordDetails, password)) {
            throw new PasswordException(PasswordError.PASSWORD_INCORRECT);
        }
    }


    /**
     * Hash the password.
     *
     * @param stringToHash
     * @return
     */
    private String hashString(PasswordDetails passwordDetails, String stringToHash) {
        stringToHash = stringToHash + saltService.generateSaltById(passwordDetails);
        return new String(hashService.hashBase64(stringToHash));
    }

    private String hashString(SecurityDetails details, String stringToHash) {
        stringToHash = stringToHash + saltService.generateSaltById(details);
        return new String(hashService.hashBase64(stringToHash));
    }


    /**
     * Make sure the passwords match.
     *
     * @param passwordDetails
     * @param password
     * @return
     */
    private boolean matchPassword(PasswordDetails passwordDetails, String password) {
        return passwordDetails.getPasswordHashed().equals(hashString(passwordDetails, password));
    }

    private boolean matchPassword(SecurityDetails details, String password) {
        return details.getPassword().equals(hashString(details, password));
    }

    /**
     * Make sure the passwords match.
     *
     * @param passwordDetails
     * @param password
     * @return
     */
    private boolean matchHashedPassword(PasswordDetails passwordDetails, String password) {
        return passwordDetails.getPasswordHashed().equals(password);
    }

}
