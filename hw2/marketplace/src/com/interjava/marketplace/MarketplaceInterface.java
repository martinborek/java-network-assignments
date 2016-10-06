/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mborekcz
 */
public interface MarketplaceInterface extends Remote {
    public boolean registerUser(String name) throws RemoteException;
    //public void register(String name) throws RemoteException, RejectedException;
    public boolean unregisterUser(String name) throws RemoteException;
    public String[] listItems() throws RemoteException;
 //   public User getUser(String name);

    public boolean sell(String userName, UserInterface user, Account bankAccount, int price, String itemName) throws RemoteException;
    public boolean buy(String userName, Account bankAccount, String itemName) throws RemoteException;
    public boolean wish(String userName, UserInterface user, int price, String itemName) throws RemoteException;
}
