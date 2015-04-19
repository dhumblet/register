package org.cashregister.webapp.persistence.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.cashregister.domain.OverviewLine;
import org.cashregister.domain.Transaction;
import org.cashregister.webapp.persistence.api.TransactionRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by derkhumblet on 11/12/14.
 */
@Repository
public class OverviewQueries extends JpaGenericRepository<Transaction> implements TransactionRepository {

    @Override
    public void createTransaction(Transaction transaction) {
        getEntityManager().persist(transaction);
        flush();
    }

    @Override
    public long countTransactions(Date start, Date end, long merchantId) {
        TypedQuery<Long> q = getEntityManager().createQuery("SELECT count(t) FROM Transaction t WHERE date >= ?1 AND date <= ?2 AND merchant.id = ?3 AND truck = 0", Long.class);
        q.setParameter(1, start);
        q.setParameter(2, end);
        q.setParameter(3, merchantId);
        return q.getSingleResult();
    }

    @Override
    public List<Transaction> getTransactions(Date start, Date end, long merchantId) {
        TypedQuery<Transaction> q = getEntityManager().createQuery("SELECT t FROM Transaction t WHERE date >= ?1 AND date <= ?2 AND merchant.id = ?3 AND truck = 0 ORDER BY date DESC", Transaction.class);
        q.setParameter(1, start);
        q.setParameter(2, end);
        q.setParameter(3, merchantId);
        return q.getResultList();
    }

    @Override
    public Transaction readTransaction(long id) {
        TypedQuery<Transaction> q = getEntityManager().createQuery("SELECT t FROM Transaction t WHERE id = ?1", Transaction.class);
        q.setParameter(1, id);
        return q.getSingleResult();
    }

    @Override
    public long countOverviewLines(Date start, Date end, long merchantId, boolean refunds, boolean truck) {
        Query q = getEntityManager().createNativeQuery(parseQuery(STATS_COUNT_QUERY, refunds));
        q.setParameter(1, start);
        q.setParameter(2, end);
        q.setParameter(3, merchantId);
        q.setParameter(4, truck);
        return ((BigInteger) q.getSingleResult()).longValue();
    }

    @Override
    public List<OverviewLine> getOverview(Date start, Date end, long merchantId, boolean refunds, boolean truck) {
        Query q = getEntityManager().createNativeQuery(parseQuery(STATS_QUERY, refunds), "Statistic");
        q.setParameter(1, start);
        q.setParameter(2, end);
        q.setParameter(3, merchantId);
        q.setParameter(4, truck);
        return Lists.transform(q.getResultList(), new Function<Object[], OverviewLine>() {
            @Override
            public OverviewLine apply(Object[] input) {
                return new OverviewLine(input);
            }

        });
    }

    @Override
    public BigDecimal getOverviewTotal(Date start, Date end, long merchantId, boolean refunds, boolean truck) {
        Query q = getEntityManager().createNativeQuery(parseQuery(STATS_TOTAL_QUERY, refunds));
        q.setParameter(1, start);
        q.setParameter(2, end);
        q.setParameter(3, merchantId);
        q.setParameter(4, truck);
        return (BigDecimal) q.getSingleResult();
    }

    private String parseQuery(String query, boolean refunds) {
        String replaceWith = refunds ? " AND td.price < 0 " : " AND td.price >= 0 ";
        return query.replace("$refunds$", replaceWith);
    }

}
