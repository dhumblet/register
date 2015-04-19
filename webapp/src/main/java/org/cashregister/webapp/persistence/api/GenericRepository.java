package org.cashregister.webapp.persistence.api;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Generic repository for creation, read, update and delete of a specific type.
 *
 * @param <T>
 *            the generic type
 */
public interface GenericRepository<T> {

    /**
     * Persist the entity to the database. After the a successful create, the
     * passed entity comes managed.
     *
     * @param t
     *            an entity of type T
     */
    void persist(T t);

    /**
     * Find all entities.
     * <p>
     * <b>Handle with care, can query a lot of entities!</b>
     *
     * @return A list of entities
     */
    List<T> findAll();

    /**
     * Find all entities which ID is in the list of provided ID's.
     *
     * @param ids
     *            the ids to search for
     * @return the list of entities that match
     */
    List<T> findAll(List<Long> ids);

    /**
     * Delete all entities in this repository.
     * Entity cascading is not honored, and ONLY entities in this repository will be deleted.
     *
     * @return the number of entities deleted.
     */
    int deleteAll();

    /**
     * Synchronizes the object with the database. The passed entity must be
     * managed.
     *
     * @param t
     *            The t
     */
    void refresh(T t);

    /**
     * Get an instance, whose state may be lazily fetched.
     *
     * @param entityClass
     *            entity class
     * @param primaryKey
     *            primary key
     * @return the found entity instance
     */
    T getReference(Class<T> entityClass, Object primaryKey);

    /**
     * Retrieve an entity.
     *
     * @param id
     *            the id of the entity
     * @return the found entity instance or null if the entity does not exist
     */
    T find(Long id);

    /**
     * Retrieve an entity at a revision.
     *
     * @param id
     *            the id of the entity
     * @param revision
     *            the revision which will be queried against
     * @return the found entity instance or null if the entity does not exist
     */
    //	T find(Long id, Number revision);

    /**
     * Retrieve the min revision.
     *
     * @param id
     *            the id of the entity
     * @return the revision
     */
    //	Number findMinRevision(Long id);

    /**
     * Lock a managed entity using pessimistic locking (cfr.
     * {@code SELECT FOR UPDATE})
     *
     * @param id
     *            the id of the entity
     **/
    void lock(T t);

    /**
     * Merges the changed entity with the database. The passed entity can be
     * anything (new, managed, detached). After a successful update, a (new)
     * managed entity is returned.
     *
     * Please use the returned managed entity for future updates, otherwise
     * future updates might fail on detached entities.
     *
     * NOTE: Transient fields in the returned entity will be reset to null.
     *
     * @param t
     *            The entity to merge with the database
     */
    T merge(T t);

    /**
     * Remove an entity.
     *
     * @param t
     *            The entity to remove with the database
     */
    void remove(T t);

    /**
     * Gets the entity manager.
     *
     * @return the entity manager
     */
    EntityManager getEntityManager();

    /**
     * Commit the state of the entitymanager to the database immediately.
     */
    void flush();

    /**
     * Check if repository is completely empty.
     */
    boolean isEmpty();

    /**
     * Utility method to calculate the page count for a given resultset size and page size.
     *
     * @param size
     *            the size of the resultset
     * @param pageSize
     *            the size of items per page
     * @return the number of pages
     */
    int getPageCount(long size, long pageSize);
}

