/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplacejpa.dao;

import com.interjava.marketplacejpa.entities.WishVO;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Jorge
 */
public class WishDao {
    public static List<WishVO> get(EntityManager em, String name) {
        List<WishVO> wishes = em.createNamedQuery("wishByName", WishVO.class)
                .setParameter("name", name)
                .getResultList();
        return wishes;
    }
}
