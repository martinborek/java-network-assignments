/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.converter.view;

import com.interjava.converter.controller.ConverterController;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author mborekcz
 */
@Named("ratesManager")
@ConversationScoped
public class RatesManager implements Serializable{
    private static final long serialVersionUID = 1L;
    @EJB
    private ConverterController controller;
    private String currencyFrom; 
    private String currencyTo; 
    private Integer originalAmount;
    private String convertedAmount;

    @Inject
    private Conversation conversation;

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
    }

    /**
     * This return value is needed because of a JSF 2.2 bug. Note 3 on page 7-10 of the JSF 2.2 specification states
     * that action handling methods may be void. In JSF 2.2, however, a void action handling method plus an if-element
     * that evaluates to true in the faces-config navigation case causes an exception.
     *
     * @return an empty string.
     */
    private String jsf22Bugfix() {
        return "";
    }

 
    public String convert()
    {
        try {
            startConversation();
            Double currencyFromValue = controller.getCurrency(currencyFrom).getRate();
            Double currencyToValue = controller.getCurrency(currencyTo).getRate();
            Double convertedAmountValue = originalAmount * (currencyToValue/currencyFromValue);
            convertedAmount = String.format("%.2f", convertedAmountValue);
        } catch (Exception e) {
            handleException(e);
        }

        return jsf22Bugfix();
    }
    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public Integer getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Integer originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(String convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
 
    
}
