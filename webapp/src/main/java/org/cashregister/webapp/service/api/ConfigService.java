package org.cashregister.webapp.service.api;

import java.math.BigDecimal;

/**
 * Created by derkhumblet on 28/11/15.
 */
public interface ConfigService {

    BigDecimal electronicPaymentLimitForMerchant(Long merchantId);
    BigDecimal electronicPaymentCostForMerchant(Long merchantId);

}
