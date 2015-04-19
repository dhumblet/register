package org.cashregister.webapp.service.impl;

import org.cashregister.domain.OverviewLine;
import org.cashregister.webapp.persistence.api.TransactionRepository;
import org.cashregister.webapp.service.api.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by derkhumblet on 02/03/15.
 */
@Service
public class OverviewServiceImpl implements OverviewService {
    @Autowired private TransactionRepository repo;


    @Override @Transactional
    public long countOverview(Date start, Date end, long merchantId, boolean refunds, boolean truck) {
        return repo.countOverviewLines(start, end, merchantId, refunds, truck);
    }

    @Override @Transactional
    public List<OverviewLine> getOverview(Date start, Date end, long merchantId, boolean refunds, boolean truck) {
        return repo.getOverview(start, end, merchantId, refunds, truck);
    }

    @Override @Transactional
    public BigDecimal getOverviewTotal(Date start, Date end, long merchantId, boolean refunds, boolean truck) {
        return repo.getOverviewTotal(start, end, merchantId, refunds, truck);
    }

}
