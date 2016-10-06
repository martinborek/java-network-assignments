/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.converter.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author mborekcz
 */
@Entity
public class Currency implements Serializable, CurrencyDTO {

    private static final long serialVersionUID = 1L;
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private String id;
    //private String name;
    private Double rate;

    public Currency() {

    }

    public Currency(String id, Double rate)
    {
        this.id = id;
        this.rate = rate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.interjava.converter.model.Currency[ id=" + id + " ]";
    }

    @Override
    public Double getRate() {
        return rate;
    }

    public void setNate(Double rate) {
        this.rate = rate;
    }
   /** 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
*/
    
}
