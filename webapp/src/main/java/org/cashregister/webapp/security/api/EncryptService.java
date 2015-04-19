package org.cashregister.webapp.security.api;

/**
 * Created by derkhumblet on 17/01/15.
 */
public interface EncryptService {

    String encrypt(String input);

    String decrypt(byte[] input);
}
