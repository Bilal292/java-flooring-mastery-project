package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlooringMasterServiceImpl implements FlooringMasteryService{

    private OrderDao orderDao;
    private TaxDao taxDao;
    private ProductDao productDao;
    private ExportDao exportDao;
    private AuditDao auditDao;

    public FlooringMasterServiceImpl(OrderDao orderDao, TaxDao taxDao, ProductDao productDao,
                                     ExportDao exportDao, AuditDao auditDao) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.exportDao = exportDao;
        this.auditDao = auditDao;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws PersistenceException {
        return orderDao.getOrdersForDate(date);
    }

    @Override
    public boolean isFutureDate(LocalDate date){
        LocalDate currentDate = LocalDate.now();
        return date.isAfter(currentDate);
    }

    @Override
    public boolean isValidName(String name) {
        // Regular expression pattern for the given criteria
        String regex = "^[a-zA-Z0-9,.\\s]+$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(name);

        // Check if the name matches the pattern and is not blank
        return matcher.matches() && !name.trim().isEmpty();
    }

    @Override
    public boolean isValidArea(String area) {
        try {
            double areaValue = Double.parseDouble(area);
            if (areaValue <= 0) {
                return false;
            } else if (areaValue < 100) {
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isValidStateName(String state) throws PersistenceException {
        taxDao.loadFile();
        return taxDao.stateInFile(state);
    }

    @Override
    public List<Product> getProducts() throws PersistenceException {
        productDao.loadFile();
        return productDao.getAllProducts();
    }

    @Override
    public int getNextOrderNumber() throws PersistenceException {
        return orderDao.getNextOrderNumber();
    }

    @Override
    public BigDecimal getTaxRate(String name) throws PersistenceException {
        return taxDao.getTaxForState(name);
    }

    @Override
    public void addOrder(Order order) throws PersistenceException {
        orderDao.addOrder(order);
        auditDao.writeAuditEntry("Order " + order.getOrderNumber() + " CREATED.");
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException {
        return orderDao.getOrder(orderDate, orderNumber);
    }

    @Override
    public void editOrder(Order order) throws PersistenceException, NoSuchOrderException {
        orderDao.editOrder(order);
        auditDao.writeAuditEntry("Order " + order.getOrderNumber() + " EDITED.");
    }

    @Override
    public BigDecimal calculateTax(Order order) throws PersistenceException {
        BigDecimal matCostAddLabCost = order.getMaterialCost().add(order.getLabourCost());
        BigDecimal taxRate = this.getTaxRate(order.getState());
        BigDecimal divideBy = new BigDecimal("100");
        BigDecimal taxPercent = taxRate.divide(divideBy);
        BigDecimal tax = matCostAddLabCost.multiply(taxPercent);
        return tax;
    }

    @Override
    public void removeOrder(LocalDate orderDate, int orderNumber) throws PersistenceException, NoSuchOrderException {
        orderDao.removeOrder(orderDate, orderNumber);
        auditDao.writeAuditEntry("Order " + orderNumber + " REMOVED.");
    }

    @Override
    public void exportData() throws PersistenceException {
        exportDao.exportData(orderDao.getAllOrders());
    }

}
