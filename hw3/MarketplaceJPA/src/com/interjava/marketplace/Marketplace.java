package com.interjava.marketplace;

import com.interjava.marketplacejpa.dao.ItemDao;
import com.interjava.marketplacejpa.dao.UserDao;
import com.interjava.marketplacejpa.dao.WishDao;
import com.interjava.marketplacejpa.entities.ItemVO;
import com.interjava.marketplacejpa.entities.UserVO;
import com.interjava.marketplacejpa.entities.WishVO;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import se.kth.id2212.ex3.bankjpa.Bank;
import se.kth.id2212.ex3.bankjpa.Account;
import se.kth.id2212.ex3.bankjpa.Owner;
import se.kth.id2212.ex3.bankjpa.RejectedException;

/**
 *
 * @author mborekcz
 */
@SuppressWarnings("serial")
public class Marketplace extends UnicastRemoteObject implements MarketplaceInterface {

    private static final String BANK_NAME = "Nordea";

    private Map<String, UserInterface> userCallbacks = new HashMap<>();
    private Bank bank;
    private final EntityManagerFactory emFactory;

    public Marketplace() throws RemoteException, NotBoundException, MalformedURLException {
        super();
        emFactory = Persistence.createEntityManagerFactory("market");
        bank = (Bank) Naming.lookup(BANK_NAME);
    }

    @Override
    public boolean registerUser(String userName, String password, UserInterface userCallback) throws RemoteException {
        System.out.println("registerUser "+userName);
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        if (UserDao.userExists(em, userName)) {
            return false;
        }

        UserVO newUser = new UserVO();
        newUser.setName(userName);
        newUser.setPassword(password);
        newUser.setItemsBought(0);
        newUser.setItemsSold(0);
        em.persist(newUser);
        em.getTransaction().commit();

        userCallbacks.put(userName, userCallback);

        return true;
    }

    @Override
    public boolean unregisterUser(String userName) throws RemoteException {
        System.out.println("unregisterUser "+userName);
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            userCallbacks.remove(userName);
            UserVO user = UserDao.getUserByName(em, userName);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public String[] listItems() throws RemoteException {
        System.out.println("listItems");
        EntityManager em = emFactory.createEntityManager();

        List<ItemVO> items = ItemDao.listAll(em);
        String[] itemList = new String[items.size()];
        int i = 0;
        for (ItemVO item : items) {
            itemList[i++] = itemString(item.getName(), "" + item.getPrice());
        }
        return itemList;
    }

    public static String itemString(String name, String price) {
        return name + " (" + price + "Kr)";
    }

    @Override
    public boolean sell(String userName, int price, String itemName) throws RemoteException {
        System.out.println("sell "+itemName);
        EntityManager em = emFactory.createEntityManager();
        //verify if the user exists and if the item exists
        ItemVO item = ItemDao.get(em, itemName);
        if (!UserDao.userExists(em, userName) || item != null) {
            return false;
        }
        em.getTransaction().begin();
        try {

            //create item and store it
            item = new ItemVO();
            item.setName(itemName);
            item.setPrice(price);
            UserVO seller = UserDao.getUserByName(em, userName);
            item.setSeller(seller);
            em.persist(item);

            // Check if someone has it in their wishlist
            List<WishVO> wishes = WishDao.get(em, itemName);

            if (wishes.size() > 0) {
                for (WishVO wish : wishes) {
                    //notify if the wish came true
                    if (wish.getPrice() >= price) {
                        UserInterface userCallback = userCallbacks.get(wish.getMarketUser().getName());
                        if(userCallback != null) {
                            userCallback.notifyAvailable(price, itemName);
                        }
                        // remove from wishlist when notification sent
                        em.remove(wish);
                    }
                }
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
        return true;
    }

    @Override
    public boolean buy(String buyerName, String itemName) throws RemoteException {
        Account bankAccount = bank.findAccount(buyerName);
        System.out.println("buy "+itemName);
        EntityManager em = emFactory.createEntityManager();

        //get item
        ItemVO item = ItemDao.get(em, itemName);
        //verify if the user exists and if the item exists
        if (!UserDao.userExists(em, buyerName) || item == null) {
            return false;
        }

        em.getTransaction().begin();
        try {
            //withdraw from the account if available
            if (bankAccount != null) {
                // bankAccount for customer is created, withdraw money
                bankAccount.withdraw(item.getPrice());
            }
            //get seller bank account
            String sellerName = item.getSeller().getName();
            Account sellerAccount = bank.findAccount(sellerName);
            // if bankAccount for seller exists, deposit money
            if (sellerAccount != null) {
                sellerAccount.deposit(item.getPrice());
            }

            //Notify seller
            UserInterface sellerCallback = userCallbacks.get(sellerName);
            if(sellerCallback != null) {
                sellerCallback.notifySold(item.getPrice(), itemName);
            }

            //Delete item
            em.remove(item);
            
            //Register user activity
            UserDao.incrementItemsSold(em,sellerName);
            UserDao.incrementItemsBought(em,buyerName);
            
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }

        return true;
    }

    @Override
    public boolean wish(String userName, int price, String itemName) throws RemoteException {
        System.out.println("wish "+itemName);

        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            if (!UserDao.userExists(em, userName)) {
                return false;
            }

            //inserts or updates a wish
            List<WishVO> wishes = em.createNamedQuery("wishByNameAndUser", WishVO.class)
                    .setParameter("itemName", itemName)
                    .setParameter("userName", userName)
                    .getResultList();
            WishVO wish;
            if (wishes.size() > 0) {
                wish = wishes.get(0);
                wish.setPrice(price);
            } else {
                wish = new WishVO();
                wish.setName(itemName);
                wish.setPrice(price);
                UserVO userVO = UserDao.getUserByName(em, userName);
                wish.setMarketUser(userVO);
            }
            em.persist(wish);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }

        return true;
    }

    @Override
    public boolean login(String userName, String password, UserInterface userCallback) throws RemoteException {
        System.out.println("login "+userName);
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        UserVO user = UserDao.login(em, userName, password);
        if (user == null) {
            return false;
        } else {
            userCallbacks.put(userName, userCallback);
            return true;
        }
    }

    @Override
    public boolean logout(String userName) throws RemoteException {
        System.out.println("logout "+userName);
        EntityManager em = emFactory.createEntityManager();
        if (!UserDao.userExists(em, userName)) {
                return false;
            }
        userCallbacks.remove(userName);
        return true;
    }

    @Override
    public String userInfo(String userName) throws RemoteException {
        EntityManager em = emFactory.createEntityManager();
        UserVO user = UserDao.getUserByName(em, userName);
        String userInfo = "";
        
        userInfo += "Items Bought: "+user.getItemsBought()+"\n"
                 +  "Items Sold: "+user.getItemsSold();
        
        return userInfo;
    }

}
