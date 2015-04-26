package org.cashregister.webapp.pages.charts;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.cashregister.webapp.util.DateHelper;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractCountPanel extends AbstractChartJsPanel {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCountPanel.class);
    private static final long ONE_DAY = 1;
    private static final long ONE_MONTH = 32;
    private static final long SIX_MONTHS = 183;
    private static final long FIVE_YEARS = 1826;
    private final Locale locale;
    protected IModel<Date> startDate;
    protected IModel<Date> endDate;
    protected long merchantId;

    public AbstractCountPanel(String id, long merchantId, IModel<Date> startDate, IModel<Date> endDate, Locale locale) {
        super(id);
        this.locale = locale;
        initPanel();
        this.startDate = startDate;
        this.endDate = endDate;
        this.merchantId = merchantId;
    }

    private void initPanel() {
        setOutputMarkupId(true);
        WebMarkupContainer container = new WebMarkupContainer(getName());
        container.setOutputMarkupId(true);
        add(container);
    }

    @Override
    protected ChartType getChartType() {
        return ChartType.LINE;
    }

    /**
     * Fill missing dates with 0 value.
     * @param dbData
     * @param grouper
     * @return
     */
    protected Map<String, Integer> fillMissingDates(Map<String, Integer> dbData, TimeGrouper grouper) {
        // Configure date patterns
        DateFormat inputDF = getInputDF(grouper);
        DateFormat outputDF = getOutputDF(grouper);
        Map<String, Integer> data = new LinkedHashMap<>();
        // Fill all dates to be shown
        DateTime start = new DateTime(startDate.getObject());
        DateTime end = new DateTime(endDate.getObject());
        for (DateTime date = start; date.isBefore(end); date = addTimeStep(date, grouper)) {
            data.put(outputDF.format(date.toDate()), 0);
        }
        // Add all found data
        for (Map.Entry<String, Integer> entry : dbData.entrySet()) {
            DateTime datetime;
            try {
                datetime = new DateTime(inputDF.parse(entry.getKey()));
            } catch (ParseException e) {
                LOG.error("Unable to parse {}", entry.getKey());
                break;
            }
            String key = outputDF.format(datetime.toDate());
            if (data.containsKey(key)) {
                data.put(key, data.get(key) + entry.getValue());
            } else {
                data.put(key, entry.getValue());
            }

        }
        return data;
    }

    /**
     * Fill missing dates with 0 value.
     * @param dbData
     * @param grouper
     * @return
     */
    protected Map<String, Float> fillMissingDatesFloat(Map<String, Float> dbData, TimeGrouper grouper) {
        // Configure date patterns
        DateFormat inputDF = getInputDF(grouper);
        DateFormat outputDF = getOutputDF(grouper);
        Map<String, Float> data = new LinkedHashMap<>();
        // Fill all dates to be shown
        DateTime start = new DateTime(startDate.getObject());
        DateTime end = new DateTime(endDate.getObject());
        for (DateTime date = start; date.isBefore(end); date = addTimeStep(date, grouper)) {
            data.put(outputDF.format(date.toDate()), 0f);
        }
        // Add all found data
        for (Map.Entry<String, Float> entry : dbData.entrySet()) {
            DateTime datetime;
            try {
                datetime = new DateTime(inputDF.parse(entry.getKey()));
            } catch (ParseException e) {
                LOG.error("Unable to parse {}", entry.getKey());
                break;
            }
            String key = outputDF.format(datetime.toDate());
            if (data.containsKey(key)) {
                data.put(key, data.get(key) + entry.getValue());
            } else {
                data.put(key, entry.getValue());
            }

        }
        return data;
    }

    private DateTime addTimeStep(DateTime date, TimeGrouper grouper) {
        switch (grouper) {
            default:
            case DAYS:
                return date.plusDays(1);
            case HOURS:
                return date.plusHours(1);
            case WEEKS:
                return date.plusWeeks(1);
            case MONTHS:
                return date.plusMonths(1);
            case YEARS:
                return date.plusYears(1);
        }
    }

    private DateFormat getInputDF(TimeGrouper grouper) {
        switch (grouper) {
            default:
            case DAYS:
                return new SimpleDateFormat("yyyyMMdd", locale);
            case HOURS:
                return new SimpleDateFormat("yyyyMMddHH", locale);
            case WEEKS:
                return new SimpleDateFormat("yyyyww", locale);
            case MONTHS:
                return new SimpleDateFormat("yyyyMM", locale);
            case YEARS:
                return new SimpleDateFormat("yyyy", locale);
        }
    }

    private DateFormat getOutputDF(TimeGrouper grouper) {
        switch (grouper) {
            default:
            case DAYS:
                return new SimpleDateFormat("EE d MMM", locale);
            case HOURS:
                return new SimpleDateFormat("H'h'", locale);
            case WEEKS:
                return new SimpleDateFormat("'w'ww", locale);
            case MONTHS:
                return new SimpleDateFormat("MM/yy", locale);
            case YEARS:
                return new SimpleDateFormat("yyyy", locale);
        }
    }

    protected TimeGrouper calculateGroupByTime() {
        long difference = (new Duration(new DateTime(startDate.getObject()), new DateTime(endDate.getObject()))).getStandardDays();
        if (difference < ONE_DAY) {
            return TimeGrouper.HOURS;
        }
        if (difference <= ONE_MONTH) {
            return TimeGrouper.DAYS;
        }
        if (difference <= SIX_MONTHS) {
            return TimeGrouper.WEEKS;
        }
        if (difference <= FIVE_YEARS) {
            return TimeGrouper.MONTHS;
        }
        return TimeGrouper.YEARS;
    }
}
