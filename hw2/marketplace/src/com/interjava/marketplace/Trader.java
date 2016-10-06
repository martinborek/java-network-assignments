/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.StringTokenizer;

/**
 *
 * @author mborekcz
 */
public class Trader {
    private static final String USAGE = "java marketplace.Trader <marketplace_url>";
    private static final String HELP = "Commands:\nregister <name>\nunregister\nsell <item> <price>\n"
        + "buy <item>\nwish <item> <price>\nlist\nbank\nexit\nhelp\n";
    private String marketName;
    MarketplaceInterface marketplace;
    User user;
    String userName;
    boolean exitApplication;

    private static final String bankName = "Nordea";
    Bank bankobj;
    Account bankAccount;



    public Trader() {
        this(Server.MARKETPLACE);
    }

    public Trader(String marketName) {
        this.marketName = marketName;
        exitApplication = false;
        userName = null;
        try {
            try {
                LocateRegistry.getRegistry(1099).list(); //1099 - default port
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }
            marketplace = (MarketplaceInterface) Naming.lookup(marketName);
        } catch (Exception e) {
            System.err.println("Connecting to marketplace " + marketName + " failed (" + e.getMessage() + ")");
            System.exit(0);
        }
        System.out.println("Welcome to marketplace: " + marketName);
         
        // connect to bank
        try {
            try {
                LocateRegistry.getRegistry(1099).list(); //1099 - default port
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(1099);
            }
            bankobj = (Bank) Naming.lookup(bankName);
        } catch (Exception e) {
            System.err.println("Connecting to bank " + bankName + " failed (" + e.getMessage() + ")");
            System.exit(0);
        }


    }

    public void run() {
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        while (!exitApplication) {
            if (userName == null)
                System.out.print("[@" + marketName + "] ");
            else
                System.out.print("[" + userName + "@" + marketName + "] ");
            try {
                String input = consoleIn.readLine();
                execute(decode(input));
            } catch (IOException e) {
                System.out.println("Processing user's command failed");
                e.printStackTrace();
            }
        }
    }

    private Command decode(String input) {
        //Command.CommandName commandName =  Command.CommandName.wrongCommand;
        Command.CommandName commandName =  null;
        String item = null;
        int price = 0;
        String newUserName = null;
        boolean wrongFormat = false;

        if (input == null)
            return null;

        StringTokenizer tokenizer = new StringTokenizer(input);
        if (tokenizer.countTokens() == 0)
            return null;

        try {
            String commandNameString = tokenizer.nextToken();
            commandName = Command.CommandName.valueOf(Command.CommandName.class, commandNameString);
        } catch (IllegalArgumentException commandDoesNotExist) {
            System.out.println("This command does not exist. For help write \"help\".");
            return null;
        }

        switch (commandName) {
            case register:
                if (tokenizer.countTokens() != 1)
                    wrongFormat = true;
                else
                    newUserName = tokenizer.nextToken();
                break;
            case unregister:
            case list:
            case exit:
            case help:
            case bank:
                if (tokenizer.countTokens() != 0) 
                    wrongFormat = true;
                break;
            case sell:
            case wish:
                if (tokenizer.countTokens() != 2)
                    wrongFormat = true;
                else {
                    item = tokenizer.nextToken();
                    try {
                        price = Integer.parseUnsignedInt(tokenizer.nextToken());
                    } catch (NumberFormatException e) {
                        wrongFormat = true;
                    }
                }
                break;
            case buy:
                if (tokenizer.countTokens() != 1)
                    wrongFormat = true;
                else
                    item = tokenizer.nextToken();
                break;
        }

        if (wrongFormat) {
            System.out.println("Wrong command format. For help write \"help\"."); 
            return null;
        }

        return new Command(commandName, newUserName, item, price);
    }

    private void execute(Command command) throws RemoteException
    {
        if (command == null)
            return;

        switch (command.getCommandName()) {
            case exit:
                exitApplication = true;
                return;
                //break;

            case help:
                System.out.println(HELP);
                return;
                //break;

            case unregister:
                if (userName == null) {
                    System.out.println("You aren't registered.");
                }
                else {
                    marketplace.unregisterUser(userName);
                    userName = null;
                }
                break;
               
            case register:
                if (userName != null) {
                    System.out.println("You are already registered.");
                }
                else {
                    if (marketplace.registerUser(command.getUserName()))
                    {
                        System.out.println("You've been successfully registered.");
                        userName = command.getUserName();
                        user = new User();
                    }
                    else 
                        System.out.println("This username cannot be registered.");
                }
                break;
                
            case list:
                for (String item : marketplace.listItems()) {
                    System.out.println(item);
                }
                break;

            case sell:
                if (userName == null) {
                    System.out.println("You need to be registered.");
                }
                else {  
                    if (marketplace.sell(userName, user, bankAccount, command.getPrice(), command.getItem())) 
                        System.out.println("Item successfully placed to the marketplace.");
                    else 
                        System.out.println("This item cannot be placed to the marketplace. Item with this name already exists.");
                }
                break;

            case buy:
                if (userName == null) {
                    System.out.println("You need to be registered.");
                }
                else {
                    if (marketplace.buy(userName, bankAccount, command.getItem()))
                        System.out.println("Item successfully bought.");
                    else
                        System.out.println("This item is not available or you don't have enough money in your account.");
                }
                break;

            case wish: 
                if (userName == null) {
                    System.out.println("You need to be registered.");
                } 
                else {
                    if (marketplace.wish(userName, user, command.getPrice(), command.getItem())) 
                        System.out.println("Wish successfully placed to the marketplace.");
                }
                break;

            case bank:
                if (userName == null) {
                    System.out.println("You need to be registered.");
                } 
                else if (bankAccount != null) {
                    System.out.println("You already have a bank account.");
                }
                else {
                    try {
                        bankAccount = bankobj.newAccount(userName);
                        System.out.println("Bank account successfully created.");
                    } catch (RejectedException ex) {
                                System.out.println("Cannot create bank account because account with this name already exists");
                    }
                }
                break;
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ((args.length > 1) || (args.length > 0 && args[0].equals("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String marketName;
        if (args.length > 0) {
            marketName = args[0];
            new Trader(marketName).run();
        } else {
            new Trader().run();
        }
    }
    
}
