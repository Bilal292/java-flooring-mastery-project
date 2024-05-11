package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> getOrdersForDate(LocalDate date) throws PersistenceException;

    int getNextOrderNumber() throws PersistenceException;

    void addOrder(Order order) throws PersistenceException;

    Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException;

    void editOrder(Order order) throws PersistenceException, NoSuchOrderException;

    void removeOrder(LocalDate orderDate, int orderNumber) throws PersistenceException, NoSuchOrderException;

    Map<LocalDate,Map<Integer, Order>> getAllOrders() throws PersistenceException;
}
