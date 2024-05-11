package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.NoSuchOrderException;
import com.sg.flooringmastery.dao.PersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryService {
    List<Order> getOrdersForDate (LocalDate date) throws PersistenceException;

    boolean isFutureDate (LocalDate date);

    boolean isValidName(String name);

    boolean isValidArea(String area);

    boolean isValidStateName(String state) throws PersistenceException;

    int getNextOrderNumber() throws PersistenceException;

    List<Product> getProducts () throws PersistenceException;

    BigDecimal getTaxRate(String name) throws PersistenceException;
    void addOrder(Order order) throws PersistenceException;

    Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException;

    void editOrder(Order order) throws PersistenceException, NoSuchOrderException;

    BigDecimal calculateTax(Order order) throws PersistenceException;

    void removeOrder(LocalDate orderDate, int orderNumber) throws PersistenceException, NoSuchOrderException;

    void exportData() throws PersistenceException;

}
