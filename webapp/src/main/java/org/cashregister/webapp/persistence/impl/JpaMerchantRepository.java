package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.Merchant;
import org.cashregister.webapp.persistence.api.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Repository
public class JpaMerchantRepository extends JpaGenericRepository<Merchant> implements MerchantRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JpaMerchantRepository.class);

    @Override
    public void createMerchant(Merchant merchant) {
        getEntityManager().persist(merchant);
        getEntityManager().flush();
    }
}
