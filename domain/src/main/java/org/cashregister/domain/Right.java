package org.cashregister.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Entity
@Table(name="rights")
public class Right extends GenericEntity implements Serializable {

    private String name;

    public Right() { }

    public Right(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
