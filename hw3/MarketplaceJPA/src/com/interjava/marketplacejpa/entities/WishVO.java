/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplacejpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
            name = "wishByName",
            query = "select w from Wish w "
                    + "where w.name LIKE :name "
    ),
    @NamedQuery(
            name = "wishByNameAndUser",
            query = "select w from Wish w "
                    + "where w.name LIKE :itemName "
                    + "and w.marketUser.name LIKE :userName "
    )
})
@Entity(name = "Wish")
@Table(
        uniqueConstraints= @UniqueConstraint(columnNames={"name", "MARKETUSER_ID"})
)
public class WishVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer price;
    
    @ManyToOne
    private UserVO marketUser;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserVO getMarketUser() {
        return marketUser;
    }

    public void setMarketUser(UserVO marketUser) {
        this.marketUser = marketUser;
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
        if (!(object instanceof WishVO)) {
            return false;
        }
        WishVO other = (WishVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.interjava.marketplacejpa.entities.Wish[ id=" + id + " ]";
    }
    
}
