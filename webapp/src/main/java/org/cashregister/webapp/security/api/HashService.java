package org.cashregister.webapp.security.api;

import java.security.Key;
import java.security.MessageDigest;

/**
 * Created by derkhumblet on 12/01/15.
 */
public interface HashService {
    /**
     * Builds the message digest.
     *
     * @param digestAlgorithm the digest algorithm
     * @return the message digest
     * @throws SecurityException if the MessageDigest can't be build
     */
    MessageDigest buildMessageDigest(String digestAlgorithm) throws SecurityException;

    /**
     * Gets the hMAC.
     *
     * @param key the key
     * @param byteBlock the byte block
     * @param macAlgorithm the MAC algorithm
     * @return the HMAC of the byteBlock
     * @throws SecurityException if the HMAC can't be retrieved
     */
    byte[] getHMAC(Key key, byte[] byteBlock, String macAlgorithm) throws SecurityException;

    /**
     * Hash.
     *
     * @param stringToHash the string to hash
     * @return the byte[]
     */
    public byte[] hash(String stringToHash);

    /**
     * Hash base64.
     *
     * @param stringToHash the string to hash
     * @return the byte[]
     */
    public byte[] hashBase64(String stringToHash);
}
