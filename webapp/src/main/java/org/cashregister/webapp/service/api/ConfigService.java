package org.cashregister.webapp.service.api;

import java.math.BigDecimal;

/**
 * Created by derkhumblet on 28/11/15.
 */
public interface ConfigService {

    BigDecimal electronicPaymentLimit();
    BigDecimal electronicPaymentCost();

}
