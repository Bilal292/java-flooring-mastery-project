package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.util.List;

public interface TaxDao {
    void loadFile() throws PersistenceException;

    List<Tax> getAllTaxes();
    BigDecimal getTaxForState(String name) throws PersistenceException;

    boolean stateInFile(String state) throws PersistenceException;
}
