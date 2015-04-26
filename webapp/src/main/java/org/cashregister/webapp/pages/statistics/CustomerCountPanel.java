package org.cashregister.webapp.pages.statistics;

import org.apache.wicket.model.IModel;
import org.cashregister.webapp.pages.charts.AbstractCountPanel;
import org.cashregister.webapp.pages.charts.TimeGrouper;
import org.cashregister.webapp.persistence.api.StatisticsRepository;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by derkhumblet on 25/04/15.
 */
public class CustomerCountPanel extends AbstractCountPanel {
    private StatisticsRepository repository;

    public CustomerCountPanel(String id, StatisticsRepository repo, long merchantid, IModel<Date> startDate, IModel<Date> endDate, Locale locale) {
        super(id, merchantid, startDate, endDate, locale);
        setOutputMarkupId(true);
        this.repository = repo;
    }

    @Override
    protected String getName() {
        return "customerCountChart";
    }

    @Override
    protected Map<String, Float> getData() {
        TimeGrouper grouper = calculateGroupByTime();
        Map<String, Float> customerCounts = repository.getCustomerCount(merchantId,
                startDate.getObject(),
                endDate.getObject(),
                grouper);
        return fillMissingDatesFloat(customerCounts, grouper);
    }
}
