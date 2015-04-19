package org.cashregister.webapp.security.impl;

import org.cashregister.webapp.security.api.EncryptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

/**
 * Created by derkhumblet on 17/01/15.
 */
@Service
public class EncryptServiceImpl implements EncryptService {
    private static final Logger LOG = LoggerFactory.getLogger(EncryptServiceImpl.class);
    private static final String algorithm = "RC2";
    private Key symKey;
    private Cipher cipher;

    public EncryptServiceImpl() {
        try {
            symKey = KeyGenerator.getInstance(algorithm).generateKey();
            cipher = Cipher.getInstance(algorithm);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }


    @Override
    public String encrypt(String input) {
        if (cipher == null) {
            return null;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, symKey);
            return new String(cipher.doFinal(input.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String decrypt(byte[] input) {
        if (cipher == null) {
            return null;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, symKey);
            byte[] decrypt = cipher.doFinal(input);
            String decrypted = new String(decrypt);
            return decrypted;
        } catch (Exception e) {
            return null;
        }
    }
}
