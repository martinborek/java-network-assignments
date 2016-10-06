/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author mborekcz
 */
public class Server {
    private static final String USAGE = "java marketplace.Server <bank_rmi_url>";
    public static final String MARKETPLACE = "MyMarketplace";

    public Server(String marketName) {
        try {
            MarketplaceInterface marketPlace = new Marketplace();
            try {
                LocateRegistry.getRegistry(1099).list(); // 1099 - default port
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }
            Naming.rebind(marketName, marketPlace);
            System.out.println("Marketplace " + marketName + " is running.");
        } catch (Exception e) {
            System.err.println("Unexpected exception");
            e.printStackTrace();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 1 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String marketName;
        if (args.length > 0) {
            marketName = args[0];
        } else {
            marketName = MARKETPLACE;
        }

        new Server(marketName);
    }
    
}
