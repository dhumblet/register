package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.TransactionDetail;
import org.cashregister.webapp.persistence.api.TransactionDetailRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by derkhumblet on 11/12/14.
 */
@Repository
public class JpaTransactionDetailRepository extends JpaGenericRepository<TransactionDetail> implements TransactionDetailRepository {

    @Override @Transactional
    public void createTransactionDetail(TransactionDetail detail) {
        getEntityManager().persist(detail);
        flush();
    }

    @Override @Transactional
    public List<TransactionDetail> findByTransaction(Long transactionId) {
        TypedQuery<TransactionDetail> q = getEntityManager().createQuery("SELECT d FROM TransactionDetail d WHERE transaction.id = ?1 AND transaction.truck = ?2", TransactionDetail.class);
        q.setParameter(1, transactionId);
        q.setParameter(2, false);
        return q.getResultList();
    }
}
