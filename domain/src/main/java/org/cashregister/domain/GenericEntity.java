package org.cashregister.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Generic entity used by the Generic repository.
 * <p>
 * The ID & version fields shouldn't be used for determination of object
 * identity.
 * <ul>
 * <li>The database identifier is only assigned <b>after</b> the entity is
 * persisted.</li>
 * <li>The version field is used to check if an entity is dirty (modified), it
 * is not part of its identity.</li>
 * </ul>
 *
 * As a result, there is no implementation of the {@link #equals()} &
 * {@link #hashCode()} methods in this class.
 * <p>
 * Because we are using all of our entities while attached to the persistence
 * context, we don't need special identifiers for working with objects in a
 * detached state.
 * <p>
 * From Bauer, King. 2010. <em>Java Persistence with Hibernate</em>: 400
 * <blockquote> You may have also noticed that the equals() and hashCode()
 * methods always access the properties of the “other” object via the getter
 * methods. This is extremely important, because the object instance passed as
 * other may be a proxy object, not the actual instance that holds the
 * persistent state. To initialize this proxy to get the property value, you
 * need to access it with a getter method. This is one point where Hibernate
 * isn’t completely transparent. However, it’s a good practice to use getter
 * methods instead of direct instance variable access anyway. </blockquote>
 */
@MappedSuperclass
public abstract class GenericEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    protected Long id;

//    @Version
//    protected Integer version = 0;

    public GenericEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Integer getVersion() {
//        return version;
//    }
//
//    public void setVersion(Integer version) {
//        this.version = version;
//    }

    public <T extends GenericEntity> boolean isSameEntity(T entity) {
        return entity.getClass().equals(getClass())

                && entity.getId() != null
                && getId() != null
                && getId().compareTo(0L) == 1
                && entity.getId().compareTo(getId()) == 0

//                && entity.getVersion() != null
//                && getVersion() != null
//                && entity.getVersion().compareTo(getVersion()) == 0
                ;
    }

    public boolean isNew() {
        return getId() == null || getId() == 0L;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append(" [id=");
        builder.append(id);
//        builder.append(", version=");
//        builder.append(version);
        builder.append("]");
        return builder.toString();
    }
}
