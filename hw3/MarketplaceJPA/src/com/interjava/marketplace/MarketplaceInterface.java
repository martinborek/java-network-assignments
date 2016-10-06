/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;
import se.kth.id2212.ex3.bankjpa.Bank;
import se.kth.id2212.ex3.bankjpa.Account;
import se.kth.id2212.ex3.bankjpa.Owner;
import se.kth.id2212.ex3.bankjpa.RejectedException;

/**
 *
 * @author mborekcz
 */
public interface MarketplaceInterface extends Remote {
    public boolean registerUser(String name, String password, UserInterface user) throws RemoteException;
    public boolean unregisterUser(String name) throws RemoteException;
    public String[] listItems() throws RemoteException;

    public boolean sell(String userName, int price, String itemName) throws RemoteException;
    public boolean buy(String userName, String itemName) throws RemoteException;
    public boolean wish(String userName, int price, String itemName) throws RemoteException;
    
    public boolean login(String userName, String password, UserInterface user) throws RemoteException;
    public boolean logout(String userName) throws RemoteException;
    public String userInfo(String userName) throws RemoteException;
    
}
