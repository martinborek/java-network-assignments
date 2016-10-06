/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mborekcz
 */
@SuppressWarnings("serial")
public class Marketplace extends UnicastRemoteObject implements MarketplaceInterface {
    private String name;
    //private Map<String, UserInterface> users = new HashMap<>();
    private Set<String> users = new HashSet<String>();
    private Map<String, Item> sellItems = new HashMap<>();
    private Map<String, Map<String, Item>> wishItems = new HashMap<>();

    public Marketplace(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public boolean registerUser(String userName) throws RemoteException {
        System.out.println("registerUser");
       /** 
        User newUser = (User) users.get(userName);

        if (newUser != null) // This username is already registered
            return false;
             
        UsernewUser = new User();
       */
        if (users.contains(userName)) // This username is already registered
            return false;

        users.add(userName);
        //users.put(userName, newUser);
        return true;
    }

    @Override
    public boolean unregisterUser(String userName) throws RemoteException {
        System.out.println("unregisterUser");

        users.remove(userName);
        return true;
    }
    
    @Override
    public String[] listItems() throws RemoteException {
        System.out.println("listItems");
        String[] list = new String[sellItems.size()];
        int i = 0;
        for (String key: sellItems.keySet()) {
            list[i++] = key + " ($" + sellItems.get(key).price + ")";
        }

        return list;
    }

    @Override
    public boolean sell(String userName, UserInterface user, Account bankAccount, int price, String itemName) throws RemoteException {
        System.out.println("sell");

        //User checkUser = (User) users.get(userName);
        Item item = sellItems.get(itemName);

        if (!users.contains(userName) || item != null)
            return false;

        Item newItem = new Item(itemName, price, userName, user, bankAccount);
        sellItems.put(itemName, newItem);

        // Check if someone has it in their wishlist

        Map<String, Item> wishItemMap = wishItems.get(itemName);
        if (wishItemMap != null) {
            for (String key: wishItemMap.keySet()) {
                Item checkItem = wishItemMap.get(key);
                if (checkItem.price >= price)
                {
                    checkItem.user.notifyAvailable(price, itemName);
                    String deletedUserName = checkItem.userName;

                    // remove from wishlist when notification sent
                    wishItemMap.remove(deletedUserName);
                    if (wishItemMap.isEmpty()) // was it the last item?
                        wishItems.remove(key);
                }
            }
        }
        
        return true;
    }

    @Override
    public boolean buy(String userName, Account bankAccount, String itemName) throws RemoteException {
        System.out.println("buy");

        Item item = sellItems.get(itemName);

        if (!users.contains(userName) || item == null)
            return false;

        if (bankAccount != null) {
            // bankAccount for customer is created, withdraw money
            try {
                bankAccount.withdraw(item.price);
            } catch (RejectedException ex) {
                return false; // not enough money for withdrawal
            }
        }

        if (item.bankAccount != null) {
            // bankAccount for seller is created, deposit money
            try {
                item.bankAccount.deposit(item.price);
            } catch (RejectedException ex) {
                System.out.println("Could not deposit money on seller's account.");
            }
        }

        
        item.user.notifySold(item.price, itemName);
        sellItems.remove(itemName);
        
        return true;
    }

    @Override
    public boolean wish(String userName, UserInterface user, int price, String itemName) throws RemoteException {
        System.out.println("wish");

        if (!users.contains(userName))
            return false;

        Map<String, Item> wishItemMap = wishItems.get(itemName);
        if (wishItemMap != null) {
            Item item = wishItemMap.get(userName);
            if (item != null) {
                item.price = price;
            }
            else {
                Item newWish = new Item(itemName, price, userName, user, null);
                wishItemMap.put(userName, newWish);
            }

        }
        else {
            Item newWish = new Item(itemName, price, userName, user, null);

            wishItemMap = new HashMap<>();
            wishItemMap.put(userName, newWish);
            
            wishItems.put(itemName, wishItemMap);
        }

        return true;
    }

    private class Item {
        String name;
        int price;
        String userName;
        UserInterface user;
        Account bankAccount;

        public Item(String name, int price, String userName, UserInterface user, Account bankAccount) {
            this.name = name;
            this.price = price;
            this.userName = userName;
            this.user = user;
            this.bankAccount = bankAccount;
        }
    }
    
}
