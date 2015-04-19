package org.cashregister.webapp.service.api;

import org.cashregister.domain.Transaction;

/**
 * Created by derkhumblet on 27/12/14.
 */
public interface KassaPrintService {

    void printTransaction(Transaction transaction);
}
