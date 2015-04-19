package org.cashregister.password;

/**
 * Created by derkhumblet on 12/01/15.
 */
public interface PasswordDetails {
    /*
	 * Should return the id of the entity implementing this interface. Used to generate a unique salt
	 */
    Long getPasswordDetailsId();

    String getPasswordHashed();

    void setPasswordHashed(String passwordHashed);

    void lock();

    void unlock();

    boolean isLocked();

    String getLogin();
}
