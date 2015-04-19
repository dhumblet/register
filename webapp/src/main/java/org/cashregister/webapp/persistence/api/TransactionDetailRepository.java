package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.TransactionDetail;

import java.util.List;

/**
 * Created by derkhumblet on 11/12/14.
 */
public interface TransactionDetailRepository extends GenericRepository<TransactionDetail> {

    void createTransactionDetail(TransactionDetail detail);

    List<TransactionDetail> findByTransaction(Long transactionId);
}
