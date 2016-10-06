package com.interjava.marketplacejpa.dao;

import com.interjava.marketplacejpa.entities.UserVO;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Jorge
 */
public class UserDao {
    public static boolean register(String name, String pass) {
        return false;
    }

    public static boolean userExists(EntityManager em, String userName) {
        List<UserVO> usersFound = em.createNamedQuery("userByName", UserVO.class)
                .setParameter("name", userName)
                .getResultList();
        if (usersFound.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static UserVO getUserByName(EntityManager em, String userName) {
        List<UserVO> usersFound = em.createNamedQuery("userByName", UserVO.class)
                .setParameter("name", userName)
                .getResultList();
        if (usersFound.size() > 0) {
            return usersFound.get(0); 
        } else {
            return null;
        }
    }
    
    
    public static UserVO login(EntityManager em, String name, String pass) {
        List<UserVO> usersFound = em.createNamedQuery("checkLogin", UserVO.class)
                .setParameter("name", name)
                .setParameter("password", pass)
                .getResultList();
        if (usersFound.size() > 0) {
            return usersFound.get(0);
        } else {
            return null;
        }
    }

    public static void incrementItemsBought(EntityManager em, String buyerName) {
        UserVO user = getUserByName(em,buyerName);
        Integer bought = user.getItemsBought();
        user.setItemsBought(bought+1);
        em.persist(user);
    }

    public static void incrementItemsSold(EntityManager em, String sellerName) {
        UserVO user = getUserByName(em,sellerName);
        Integer sold = user.getItemsSold();
        user.setItemsSold(sold+1);
        em.persist(user);
    }
}