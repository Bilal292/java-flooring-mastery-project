package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoStubImpl implements OrderDao{

    private Order order;
    private Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();
    public OrderDaoStubImpl () {
        //Creating input
        int orderNumber = 1;
        String customerName = "Bilal";
        String state = "CA";
        LocalDate orderDate = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate = new BigDecimal("25.00");
        String productType = "Tile";
        BigDecimal costPerSquareFoot = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot = new BigDecimal("4.15");
        BigDecimal materialCost = new BigDecimal("871.50");
        BigDecimal area = new BigDecimal("249.00");
        BigDecimal labourCost = new BigDecimal("1033.35");
        BigDecimal tax = new BigDecimal("476.21");
        BigDecimal total = new BigDecimal("2381.06");

        this.order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                costPerSquareFoot, labourCostPerSquareFoot, materialCost, area, labourCost, tax, total);

    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws PersistenceException {
        List<Order> toReturn = new ArrayList<>();
        toReturn.add(order);
        LocalDate orderDate = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (orderDate.equals(date)) {
            return toReturn;
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public int getNextOrderNumber() throws PersistenceException {
        return 2;
    }

    @Override
    public void addOrder(Order order) throws PersistenceException {
        return;
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException {
        LocalDate date = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (orderDate.equals(date)) {
            return order;
        }

        return null;


    }

    @Override
    public void editOrder(Order order) throws PersistenceException, NoSuchOrderException {
        return;
    }

    @Override
    public void removeOrder(LocalDate orderDate, int orderNumber) throws PersistenceException, NoSuchOrderException {
        return;
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException {
        return this.orders;
    }


}
