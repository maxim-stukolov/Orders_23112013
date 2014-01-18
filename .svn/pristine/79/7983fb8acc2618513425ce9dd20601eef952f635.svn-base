package org.orders.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@javax.persistence.Table(name = "product", schema = "", catalog = "orders")
@AttributeOverride(name = "recid", column = @Column(name = "recid",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Product extends BaseEntityAudit{

    private byte[] photo;
    @javax.persistence.Column(name = "photo")
    @Basic
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    private String name;

    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String product;

    @javax.persistence.Column(name = "product")
    @Basic
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product1 = (Product) o;

       // if (dataareaid != null ? !dataareaid.equals(product1.dataareaid) : product1.dataareaid != null) return false;
        if (name != null ? !name.equals(product1.name) : product1.name != null) return false;
        if (product != null ? !product.equals(product1.product) : product1.product != null) return false;
        if (getRecid() != null ? !getRecid().equals(product1.getRecid()) : product1.getRecid() != null) return false;
        if (getVersion() != null ? !getVersion().equals(product1.getVersion()) : product1.getVersion() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRecid() != null ? getRecid().hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
    //    result = 31 * result + (dataareaid != null ? dataareaid.hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }
}
