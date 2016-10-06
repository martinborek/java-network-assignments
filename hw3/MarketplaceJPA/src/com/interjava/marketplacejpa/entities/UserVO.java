/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplacejpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@NamedQueries({
    @NamedQuery(
            name = "checkLogin",
            query = "select u from MarketUser u "
                    + "where u.name LIKE :name "
                    + "and u.password LIKE :password "
    ),
    @NamedQuery(
            name = "userByName",
            query = "select u from MarketUser u "
                    + "where u.name LIKE :name "
    )
})

/**
 *
 * @author Jorge
 */
@Entity(name = "MarketUser")
@Table(
        uniqueConstraints= @UniqueConstraint(columnNames={"name"})
)
public class UserVO implements Serializable {

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<ItemVO> items;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String password;
    
    @Column
    private Integer itemsBought;
    
    @Column
    private Integer itemsSold;
    
    @OneToMany(mappedBy = "marketUser")
    private List<WishVO> wishes;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getItemsBought() {
        return itemsBought;
    }

    public void setItemsBought(Integer itemsBought) {
        this.itemsBought = itemsBought;
    }

    public Integer getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(Integer itemsSold) {
        this.itemsSold = itemsSold;
    }

    public List<WishVO> getWishes() {
        return wishes;
    }

    public void setWishes(List<WishVO> wishes) {
        this.wishes = wishes;
    }

    public List<ItemVO> getItems() {
        return items;
    }

    public void setItems(List<ItemVO> items) {
        this.items = items;
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
        if (!(object instanceof UserVO)) {
            return false;
        }
        UserVO other = (UserVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "marketplacejpa.entities.User[ id=" + id + " ]";
    }
    
}
