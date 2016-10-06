/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import se.kth.id2212.ex3.bankjpa.Bank;
import se.kth.id2212.ex3.bankjpa.Account;
import se.kth.id2212.ex3.bankjpa.Owner;
import se.kth.id2212.ex3.bankjpa.RejectedException;

/**
 *
 * @author Jorge
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MarketplaceTest {

    private static String userName;
    private static String password;
    private static Marketplace market;
    private static Bank bank;
    
    private static final String itemName = "apple";
    private static final int price = 10;
    
    public MarketplaceTest() {
    }
    
    @BeforeClass
    public static void init() throws Exception {
        
        String bankName = "Nordea";
        try {
            LocateRegistry.getRegistry(1099).list(); //1099 - default port
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(1099);
        }
        bank = (Bank) Naming.lookup(bankName);
        
        Random random = new Random();
        int rand = random.nextInt(Integer.MAX_VALUE);
        userName = "User"+rand;
        password = "mypass";
        market = new Marketplace();
        Account account = bank.findAccount(userName);
        account.deposit(price);
    }
    
    @AfterClass
    public static void tearDown() {
    }

    /**
     * Test of registerUser method, of class Marketplace.
     */
    @Test
    public void test1RegisterUser() throws Exception {
        System.out.println("registerUser");
        
        boolean expResult = true;
        User userCallback = new User();
        boolean result = market.registerUser(userName, password, userCallback);
        assertEquals(expResult, result);
    }

    /**
     * Test of wish method, of class Marketplace.
     */
    @Test
    public void test2Wish() throws Exception {
        System.out.println("wish");
        
        boolean expResult = true;
        boolean result = market.wish(userName, price, itemName);
        assertEquals(expResult, result);
    }

    /**
     * Test of sell method, of class Marketplace.
     */
    @Test
    public void test3Sell() throws Exception {
        System.out.println("sell");
        
        boolean expResult = true;
        boolean result = market.sell(userName, price, itemName);
        assertEquals(expResult, result);
    }

    /**
     * Test of listItems method, of class Marketplace.
     */
    @Test
    public void test4ListItems() throws Exception {
        System.out.println("listItems");
        
        String[] expResult = {market.itemString(itemName,""+price)};
        String[] result = market.listItems();
        for (String item : result) {
            System.out.println(item);
        }
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of buy method, of class Marketplace.
     */
    @Test
    public void test5Buy() throws Exception {
        System.out.println("buy");
        
        boolean expResult = true;
        boolean result = market.buy(userName, itemName);
        assertEquals(expResult, result);
    }

    /**
     * Test of unregisterUser method, of class Marketplace.
     */
    @Test
    public void test6UnregisterUser() throws Exception {
        System.out.println("unregisterUser");

        boolean expResult = true;
        boolean result = market.unregisterUser(userName);
        assertEquals(expResult, result);
    }
    
}
