package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.Config;
import org.cashregister.domain.Merchant;
import org.cashregister.domain.Product;

/**
 * Created by derkhumblet on 27/11/15.
 */
public interface ConfigRepository extends GenericRepository<Config> {


    void setConfigForKey(String key, String value, Merchant merchant);

    Config getConfigForKey(String key, Long merchantId);

}
