package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Merchant;
import org.cashregister.webapp.persistence.api.MerchantRepository;
import org.cashregister.webapp.service.api.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Service("merchantService")
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantRepository repository;

    @Override
    public Merchant createMerchant(String name) {
        Merchant merchant = new Merchant(name);
        repository.createMerchant(merchant);
        return merchant;
    }
}
