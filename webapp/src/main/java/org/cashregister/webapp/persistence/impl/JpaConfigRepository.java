package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.Config;
import org.cashregister.domain.Merchant;
import org.cashregister.webapp.persistence.api.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

/**
 * Created by derkhumblet on 27/11/15.
 */
@Repository
public class JpaConfigRepository extends JpaGenericRepository<Config> implements ConfigRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JpaProductRepository.class);

    @Transactional
    public void setConfigForKey(String key, String value, Merchant merchant) {
        Config config = getConfigForKey(key, merchant);
        if (config != null) {
            config.value(value);
            getEntityManager().merge(config);
        } else {
            getEntityManager().persist(new Config(merchant, key, value));
        }
        getEntityManager().flush();
    }

    @Transactional
    public Config getConfigForKey(String key, Merchant merchant) {
        TypedQuery<Config> q = getEntityManager().createQuery("SELECT c FROM Config c WHERE merchant = ?1 AND key = ?2", Config.class);
        q.setParameter(1, merchant);
        q.setParameter(2, key);
        return q.getSingleResult();
    }
}
