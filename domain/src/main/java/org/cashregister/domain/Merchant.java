package org.cashregister.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Entity
@Table(name="merchant")
public class Merchant extends GenericEntity implements Serializable {
    private String name;

    @Column(name = "truck", nullable = false)
    private boolean truck;

    @Column(name = "uuid")
    private String uuid;

    public Merchant() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Merchant(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTruck() {
        return truck;
    }

    public void setTruck(boolean truck) {
        this.truck = truck;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
