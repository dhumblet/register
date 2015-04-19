package org.cashregister.webapp.persistence.impl;

import org.cashregister.domain.Right;
import org.cashregister.webapp.persistence.api.RightRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Repository
public class JpaRightRepository extends JpaGenericRepository<Right> implements RightRepository {

    @Override
    public void createRight(Right right) {
        getEntityManager().persist(right);
        getEntityManager().flush();
    }
}
