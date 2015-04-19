package org.cashregister.webapp.security.impl;


import org.cashregister.webapp.security.api.SecurityService;

import java.util.List;

/**
 * Created by derkhumblet on 16/01/15.
 */
public class SecurityServiceImpl implements SecurityService {

    @Override
    public String encrypt(String data) {
        return data;
    }

    @Override
    public List<String> encrypt(List<String> data) {
        return data;
    }

    @Override
    public String decrypt(String data) {
        return data;
    }

    @Override
    public List<String> decrypt(List<String> data) {
        return data;
    }

    @Override
    public String encrypt(String data, String salt, String keyGroupName) {
        return data;
    }

    @Override
    public String decrypt(String data, String salt) {
        return data;
    }
}
