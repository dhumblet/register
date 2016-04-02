package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.Merchant;
import org.cashregister.domain.Token;
import org.cashregister.domain.User;

import java.util.List;

/**
 * Created by derkhumblet on 08/03/16.
 */
public interface TokenRepository extends GenericRepository<Token> {

    String createTokenForUser(User user, String clientId, String clientType);
    Token findToken(String token);
    List<Token> findTokensForMerchant(Merchant merchant);

}
