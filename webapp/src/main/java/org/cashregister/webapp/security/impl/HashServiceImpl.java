package org.cashregister.webapp.security.impl;

import org.cashregister.webapp.security.api.HashService;

import javax.crypto.Mac;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

/**
 * Created by derkhumblet on 12/01/15.
 */
@Service
public class HashServiceImpl implements HashService {

    @Override
    public MessageDigest buildMessageDigest(final String digestAlgorithm) throws SecurityException {
        try {
            return MessageDigest.getInstance(digestAlgorithm);
        } catch (GeneralSecurityException e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public byte[] getHMAC(final Key key, final byte[] byteBlock, final String macAlgorithm) throws SecurityException {
        try {
            Mac hmac = Mac.getInstance(macAlgorithm);
            hmac.init(key);
            return hmac.doFinal(byteBlock);
        } catch (GeneralSecurityException e) {
            throw new SecurityException(e);
        }
    }

    public byte[] hashBase64(String stringToHash) {
        byte[] digested = hash(stringToHash);
        return Base64.encodeBase64(digested);
    }

    public byte[] hash(String stringToHash) {
        MessageDigest md = buildMessageDigest("SHA-256");
        md.update(stringToHash.getBytes());
        byte[] digested = md.digest();
        return digested;
    }
}
