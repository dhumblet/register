package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.Merchant;

/**
 * Created by derkhumblet on 11/01/15.
 */
public interface MerchantRepository extends GenericRepository<Merchant> {

    void createMerchant(Merchant merchant);
}
