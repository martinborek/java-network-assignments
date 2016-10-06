/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author mborekcz
 */
public class User extends UnicastRemoteObject implements UserInterface {

    public User() throws RemoteException{
        super();
    }

    @Override
    public void notifySold(int price, String item) throws RemoteException {
        System.out.println("\nItem " + item + " was sold for $" + Integer.toString(price) + ".\n");
    }

    @Override
    public void notifyAvailable(int price, String item) throws RemoteException {
        System.out.println("\nItem " + item + " is now available for $" + Integer.toString(price) + ".\n");
    }
    
}
