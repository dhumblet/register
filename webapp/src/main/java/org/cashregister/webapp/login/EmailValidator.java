package org.cashregister.webapp.login;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Created by derkhumblet on 16/01/15.
 */
public final class EmailValidator extends PatternValidator {
    private static final EmailValidator INSTANCE = new EmailValidator();

    private EmailValidator() {
        super("^[_A-Za-z0-9-\\+]+(\\.[a-zA-Z0-9_.-]+)*@(([A-Za-z0-9]+\\.)?[A-Za-z0-9-]+)*(\\.[A-Za-z0-9]+[A-Za-z0-9.]+)$");
    }

    public static EmailValidator getInstance() {
        return INSTANCE;
    }
}
