package org.cashregister.webapp.security.api;

import java.util.List;

/**
 * Created by derkhumblet on 16/01/15.
 */
public interface SecurityService {

    String encrypt(String data);

    List<String> encrypt(List<String> data);

    String decrypt(String data);

    List<String> decrypt(List<String> data);

    String encrypt(String data, String salt, String keyGroupName);

    String decrypt(String data, String salt);
}
