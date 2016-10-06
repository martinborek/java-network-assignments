package com.interjava.marketplacejpa.dao;

import com.interjava.marketplacejpa.entities.ItemVO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Jorge
 */
public class ItemDao {

    public static void registerItem(ItemVO item) {

    }

    public static void decrementAmount(ItemVO item) {

    }

    public static ItemVO get(EntityManager em, String itemName) {
        ItemVO item;
        try {
            item = em.createNamedQuery("itemByName", ItemVO.class)
                    .setParameter("name", itemName)
                    .getSingleResult();
            return item;
        } catch (NoResultException e) {
            return null;
        }

    }

    public static List<ItemVO> listAll(EntityManager em) {
        List<ItemVO> items;
        items = em.createNamedQuery("listItems", ItemVO.class)
                .getResultList();
        return items;

    }

}
