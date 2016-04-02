package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.Merchant;
import org.cashregister.domain.Token;
import org.cashregister.domain.User;
import org.cashregister.webapp.persistence.api.TokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

/**
 * Created by derkhumblet on 08/03/16.
 */
@Repository
public class JpaTokenRepository extends JpaGenericRepository<Token> implements TokenRepository {

    @Override @Transactional
    public String createTokenForUser(User user, String clientId, String clientType) {
        Token token;
        if (hasTokenForUser(user, clientId)) {
            token = findTokenForUser(user, clientId);
            token.setToken(UUID.randomUUID().toString());
        } else {
            token = new Token(user.getMerchant(), user, UUID.randomUUID().toString(), clientId, clientType);
        }
        getEntityManager().persist(token);
        getEntityManager().flush();
        return token.getToken();
    }

    @Override @Transactional
    public Token findToken(String token) {
        try {
            TypedQuery<Token> q = getEntityManager().createQuery("SELECT t FROM Token t WHERE token LIKE ?1", Token.class);
            q.setParameter(1, token);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    private boolean hasTokenForUser(User user, String clientId) {
        TypedQuery<Long> q = getEntityManager().createQuery("SELECT count(*) FROM Token t WHERE userId LIKE ?1 AND clientId = ?2", Long.class);
        q.setParameter(1, user.getId());
        q.setParameter(2, clientId);
        return q.getSingleResult() > 0;
    }

    private Token findTokenForUser(User user, String clientId) {
        TypedQuery<Token> q = getEntityManager().createQuery("SELECT t FROM Token t WHERE userId LIKE ?1 AND clientId = ?2", Token.class);
        q.setParameter(1, user.getId());
        q.setParameter(2, clientId);
        return q.getSingleResult();
    }

    @Override @Transactional
    public List<Token> findTokensForMerchant(Merchant merchant) {
        TypedQuery<Token> q = getEntityManager().createQuery("SELECT t FROM Token t WHERE merchantId LIKE ?1", Token.class);
        q.setParameter(1, merchant.getId());
        return q.getResultList();
    }

}
