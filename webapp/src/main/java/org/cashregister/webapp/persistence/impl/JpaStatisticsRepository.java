package org.cashregister.webapp.persistence.impl;

import org.cashregister.webapp.pages.charts.TimeGrouper;
import org.cashregister.webapp.persistence.api.StatisticsRepository;
import org.cashregister.webapp.persistence.sql.StatisticsQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by derkhumblet on 25/04/15.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class JpaStatisticsRepository implements StatisticsRepository, StatisticsQueries {
    @PersistenceContext
    protected EntityManager entityManager;

    @Override @Transactional
    public Map<String, Float> getCustomerCount(long merchant, Date start, Date end, TimeGrouper grouper) {
        // Execute query
        Query query = entityManager.createNativeQuery(CUSTOMER_COUNT_QUERY);
        query.setParameter(1, grouper.getGroupByTime());
        query.setParameter(2, merchant);
        query.setParameter(3, start);
        query.setParameter(4, end);
        List<Object[]> queryResults = query.getResultList();
        return mapResultsInt(queryResults);
    }


    @Override @Transactional
    public Map<String, Float> getSaleTurnover(long merchant, Date start, Date end, TimeGrouper grouper) {
        // Execute query
        Query query = entityManager.createNativeQuery(SALE_TURNOVER_QUERY);
        query.setParameter(1, grouper.getGroupByTime());
        query.setParameter(2, merchant);
        query.setParameter(3, start);
        query.setParameter(4, end);
        List<Object[]> queryResults = query.getResultList();
        return mapResultsDecimal(queryResults);
    }

    @Override @Transactional
    public Map<String, Float> getSaleTurnover(long merchant, long category, Date start, Date end, TimeGrouper grouper) {
        // Execute query
        Query query = entityManager.createNativeQuery(SALE_TURNOVER_PER_CATEGORY_QUERY);
        query.setParameter(1, grouper.getGroupByTime());
        query.setParameter(2, merchant);
        query.setParameter(3, start);
        query.setParameter(4, end);
        query.setParameter(5, category);
        List<Object[]> queryResults = query.getResultList();
        return mapResultsDecimal(queryResults);
    }


    private Map<String, Float> mapResultsInt(List<Object[]> queryResults) {
        // Map query results
        Map<String, Float> result = new HashMap<>();
        for (Object[] queryResult : queryResults) {
            final String label = (String) queryResult[0];
            final float value = ((BigInteger) queryResult[1]).floatValue();
            result.put(label, value);
        }
        return result;
    }

    private Map<String, Float> mapResultsDecimal(List<Object[]> queryResults) {
        // Map query results
        Map<String, Float> result = new HashMap<>();
        for (Object[] queryResult : queryResults) {
            final String label = (String) queryResult[0];
            final float value = ((BigDecimal) queryResult[1]).floatValue();
            result.put(label, value);
        }
        return result;
    }

}
