package org.cashregister.webapp.pages.statistics;

import org.apache.wicket.model.IModel;
import org.cashregister.webapp.pages.charts.AbstractCountPanel;
import org.cashregister.webapp.pages.charts.TimeGrouper;
import org.cashregister.webapp.persistence.api.StatisticsRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by derkhumblet on 25/04/15.
 */
public class SaleTurnoverPanel extends AbstractCountPanel {
    private StatisticsRepository repository;

    public SaleTurnoverPanel(String id, StatisticsRepository repo, long merchantid, IModel<Date> startDate, IModel<Date> endDate, Locale locale) {
        super(id, merchantid, startDate, endDate, 0, locale);
        setOutputMarkupId(true);
        this.repository = repo;
    }

    @Override
    protected String getName() {
        return "saleTurnoverChart";
    }

    @Override
    protected Map<String, Float> getMainData() {
        TimeGrouper grouper = calculateGroupByTime();
        Map<String, Float> saleTurnover = repository.getSaleTurnover(merchantId, startDate.getObject(), endDate.getObject(), grouper);
        return fillMissingDatesFloat(saleTurnover, grouper);
    }


    @Override
    protected Map<String, Float> getSecondaryData() {
        return new HashMap();
    }
}
