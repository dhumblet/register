package org.cashregister.webapp.persistence.api;

import org.cashregister.webapp.pages.charts.TimeGrouper;

import java.util.Date;
import java.util.Map;

/**
 * Created by derkhumblet on 25/04/15.
 */
public interface StatisticsRepository {

    /**
     * @param merchant
     * @param start
     * @param end
     * @param grouper
     * @return count of customers per time interval (defined by $grouper) for $merchant between $start and $end.
     */
    Map<String, Float> getCustomerCount(long merchant, Date start, Date end, TimeGrouper grouper);

    /**
     * @param merchant
     * @param start
     * @param end
     * @param grouper
     * @return sum of transaction prices per time interval (defined by $grouper) for $merchant between $start and $end.
     */
    Map<String, Float> getSaleTurnover(long merchant, Date start, Date end, TimeGrouper grouper);

    /**
     * @param merchant
     * @param category
     * @param start
     * @param end
     * @param grouper
     * @return sum of transaction prices for $category per time interval (defined by $grouper) for $merchant between $start and $end.
     */
    Map<String, Float> getCategoryTurnover(long merchant, long category, Date start, Date end, TimeGrouper grouper);

    /**
     * @param merchant
     * @param category
     * @param start
     * @param end
     * @param grouper
     * @return count of transactions for $catgory per time interval (defined by $grouper) for $merchant between $start and $end.
     */
    Map<String, Float> getCategoryTransactionCount(long merchant, long category, Date start, Date end, TimeGrouper grouper);

}
