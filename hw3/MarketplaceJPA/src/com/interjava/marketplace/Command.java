/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.marketplace;

import java.util.EnumMap;

/**
 *
 * @author mborekcz
 */
public class Command {
    public static enum CommandName {login, logout, info, register, unregister, sell, buy, wish, list, bank, exit, help;};
    //public static EnumMap<CommandName, String> commandFormat;
    

    private String userName;
    private String password;
    private String item;
    private int price;
    private CommandName commandName;

    public Command(CommandName commandName, String userName, String item, int price, String password) {
        this.commandName = commandName;
        this.userName = userName;
        this.item = item;
        this.price = price;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public CommandName getCommandName() {
        return commandName;
    }
}