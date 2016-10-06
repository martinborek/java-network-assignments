/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.converter.controller;

import com.interjava.converter.model.Currency;
import com.interjava.converter.model.CurrencyDTO;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author mborekcz
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class ConverterController {
    @PersistenceContext(unitName = "converterPU")
    private EntityManager em;

    public CurrencyDTO getCurrency(String currency) {
        CurrencyDTO foundCurrency = em.find(Currency.class, currency);
        if (foundCurrency == null) {
            throw new EntityNotFoundException("Cannot find currency " + currency);
        }

        return foundCurrency;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
