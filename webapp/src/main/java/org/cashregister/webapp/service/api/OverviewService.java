package org.cashregister.webapp.service.api;

import org.cashregister.domain.OverviewLine;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by derkhumblet on 02/03/15.
 */
public interface OverviewService {

    long countOverview(Date start, Date end, long merchantId, boolean refunds, boolean truck);

    List<OverviewLine> getOverview(Date start, Date end, long merchantId, boolean refunds, boolean truck);

    BigDecimal getOverviewTotal(Date start, Date end, long merchantId, boolean refunds, boolean truck);
}
