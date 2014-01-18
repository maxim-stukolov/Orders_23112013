package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.Table(name = "customers", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Customer extends BaseEntityAudit{

    private String name;
    private String surname;
    private String user;

    public Customer() {

    }

    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @javax.persistence.Column(name = "surname")
    @Basic
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @javax.persistence.Column(name = "user")
    @Basic
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product1 = (Product) o;

        if (getRecid() != null ? !getRecid().equals(product1.getRecid()) : product1.getRecid() != null) return false;
        if (getVersion() != null ? !getVersion().equals(product1.getVersion()) : product1.getVersion() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid() != null ? getRecid().hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }
}
