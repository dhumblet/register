package org.cashregister.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by derkhumblet on 11/01/15.
 */
@Entity
@Table(name="role")
public class Role extends GenericEntity implements Serializable {
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Right.class)
    @JoinTable(name = "role_rights",
            joinColumns = { @JoinColumn(name = "roleId") },
            inverseJoinColumns = { @JoinColumn(name = "rightId") })
    private Set<Right> rights = new HashSet<>();

    public Role() { }

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, Set<Right> rights) {
        this.name = name;
        this.rights = rights;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Right> getRights() {
        return rights;
    }

    public void setRights(Set<Right> rights) {
        this.rights = rights;
    }
}
