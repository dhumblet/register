package org.cashregister.webapp.ws;

import org.apache.commons.codec.binary.Base64;
import org.cashregister.domain.Token;
import org.cashregister.webapp.persistence.api.TokenRepository;
import org.cashregister.webapp.security.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

/**
 * Created by derkhumblet on 18/03/16.
 */
public abstract class SecuredRestService {
    @Autowired
    private TokenRepository tokenRepository;

    protected Token validateToken(String tokenString) throws NotAuthorizedException {
        byte[] bytes = tokenString.getBytes(StandardCharsets.UTF_8);
        String decoded = new String(Base64.decodeBase64(bytes), StandardCharsets.UTF_8);
        Token token = tokenRepository.findToken(decoded);
        if (token == null) {
            throw new NotAuthorizedException();
        }
        return token;
    }
}
