package org.cashregister.webapp.service.impl;

import org.cashregister.domain.Merchant;
import org.cashregister.webapp.CashRegisterApplication;
import org.cashregister.webapp.RegisterSession;
import org.cashregister.webapp.persistence.api.ConfigRepository;
import org.cashregister.webapp.service.api.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by derkhumblet on 28/11/15.
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    static final String ELECTRONIC_PAYMENT_LIMIT = "electronic_payment_limit";
    static final String ELECTRONIC_PAYMENT_COST = "electronic_payment_cost";

    @Autowired
    private ConfigRepository configRepository;

    @Transactional
    public BigDecimal electronicPaymentLimit() {
        String configValue = configRepository.getConfigForKey(ELECTRONIC_PAYMENT_LIMIT, merchant()).value();
        return new BigDecimal(configValue);
    }

    @Transactional
    public BigDecimal electronicPaymentCost() {
        String configValue = configRepository.getConfigForKey(ELECTRONIC_PAYMENT_COST, merchant()).value();
        return new BigDecimal(configValue);
    }

    private Merchant merchant() {
        RegisterSession session = CashRegisterApplication.session();
        if (session == null) {
            System.out.println("SESSION EMPTY!");
        }
        return session.merchant();
    }
}
