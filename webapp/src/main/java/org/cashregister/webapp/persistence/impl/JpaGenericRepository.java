package org.cashregister.webapp.persistence.impl;

/**
 * Created by derkhumblet on 15/11/14.
 */

import org.cashregister.webapp.persistence.api.GenericRepository;
import org.cashregister.domain.GenericEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Generic repository that can be used as a superclass of all repositories.
 *
 * @param <T>
 *            The type of the entity managed by the repository
 */
@Transactional(propagation = Propagation.MANDATORY)
public abstract class JpaGenericRepository<T extends GenericEntity> implements GenericRepository<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> entityBeanType;

    public JpaGenericRepository() {
        this.entityBeanType = getEntityBeanType(getClass());
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityBeanType(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null) {
            throw new RuntimeException("No superclass found");
        }

        if (superClass.getTypeParameters().length == 0) {
            return getEntityBeanType(superClass);
        }

        return (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void persist(T t) {
        this.entityManager.persist(t);
    }

    @Override
    public T getReference(Class<T> entityClass, Object primaryKey) {
        return this.entityManager.getReference(entityClass, primaryKey);
    }

    @Override
    public T find(Long id) {
        return this.entityManager.find(getEntityBeanType(), id);
    }

    @Override
    public int deleteAll() {
        Query query = entityManager.createQuery("DELETE FROM " + getEntityName());
        return query.executeUpdate();
    }

    @Override
    public void lock(T t) {
        this.entityManager.lock(t, LockModeType.PESSIMISTIC_WRITE);
    }

    @Override
    public T merge(T t) {
        return this.entityManager.merge(t);
    }

    @Override
    public void remove(T t) {
        this.entityManager.remove(t);
    }

    @Override
    public void refresh(T t) {
        this.entityManager.refresh(t);
    }

    @Override
    public void flush() {
        this.entityManager.flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        Query query = this.entityManager.createQuery("select o from " + getEntityName() + " o");
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(List<Long> ids) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(getEntityBeanType());
        Root<T> entity = criteriaQuery.from(getEntityBeanType());

        ParameterExpression<List<Long>> paramList = (ParameterExpression<List<Long>>) builder.parameter(ids.getClass());

        criteriaQuery.select(entity);
        criteriaQuery.where(entity.get("id").in(paramList));

        TypedQuery<T> typedQuery = getEntityManager().createQuery(criteriaQuery);
        typedQuery.setParameter(paramList, ids);

        return typedQuery.getResultList();
    }

    public String getEntityName() {
        return getEntityBeanType().getSimpleName();
    }

    public Class<T> getEntityBeanType() {
        return this.entityBeanType;
    }

    public void setEntityBeanType(Class<T> entityBeanType) {
        this.entityBeanType = entityBeanType;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public boolean isEmpty() {
        Query query = this.entityManager.createQuery("select o from " + getEntityName() + " o");

        query.setMaxResults(1);

        return query.getResultList().isEmpty();
    }

    @Override
    public int getPageCount(long size, long pageSize) {
        double sizeWithPrecision = size;
        double pages = sizeWithPrecision / pageSize;
        pages = Math.ceil(pages);
        return (int) pages;
    }

}

