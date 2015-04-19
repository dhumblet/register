package org.cashregister.webapp.persistence.api;

import org.cashregister.domain.Right;

/**
 * Created by derkhumblet on 11/01/15.
 */
public interface RightRepository extends GenericRepository<Right> {

    void createRight(Right right);
}
