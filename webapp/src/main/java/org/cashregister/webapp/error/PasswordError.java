package org.cashregister.webapp.error;

/**
 * Created by derkhumblet on 12/01/15.
 */
public enum PasswordError {

    PASSWORD_INCORRECT(0, "The password you entered is incorrect."),
    ENTITY_LOCKED(1, "The entity is locked."),
    PASSWORD_RECENTLY_USED(2, "The password you entered is previously used and can not be used again at the moment"),
    NO_ID_FOR_SALT(3, "The entity does not have an ID to use in it's salt calculation");

    private int value;
    private String stringValue;

    private PasswordError(int value, String stringValue) {
        this.value = value;
        this.stringValue = stringValue;
    }

    public int getValue() {
        return value;
    }

    public String getStringValue() {
        return stringValue;
    }
}
