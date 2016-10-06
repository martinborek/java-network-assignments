package com.interjava.marketplacejpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Jorge
 */
@NamedQueries({
    @NamedQuery(
            name = "userProducts",
            query = "select i from Item i "
                    + "where i.seller.name LIKE :name "
    ),
    @NamedQuery(
            name = "itemByName",
            query = "select i from Item i "
                    + "where i.name LIKE :name "
    ),
    @NamedQuery(
            name = "listItems",
            query = "select i from Item i "
    )
})
@Entity(name = "Item")
@Table(
        uniqueConstraints= @UniqueConstraint(columnNames={"name"})
)
public class ItemVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private UserVO seller;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer price;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserVO getSeller() {
        return seller;
    }

    public void setSeller(UserVO seller) {
        this.seller = seller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemVO)) {
            return false;
        }
        ItemVO other = (ItemVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "marketplacejpa.entities.Item[ id=" + id + " ]";
    }
    
}
