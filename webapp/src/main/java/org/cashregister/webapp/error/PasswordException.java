package org.cashregister.webapp.error;

/**
 * Created by derkhumblet on 12/01/15.
 */
public class PasswordException extends RuntimeException {

    private static final long serialVersionUID = 8280382247856070518L;

    private PasswordError passwordFrameworkError;

    public PasswordException(PasswordError passwordFrameworkError) {
        this.passwordFrameworkError = passwordFrameworkError;
    }

    public PasswordError getPasswordFrameworkError() {
        return passwordFrameworkError;
    }
}
