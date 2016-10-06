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
public interface UserInterface extends Remote {

    public void notifySold(int price, String item) throws RemoteException;
    public void notifyAvailable(int price, String item) throws RemoteException;
    
}
