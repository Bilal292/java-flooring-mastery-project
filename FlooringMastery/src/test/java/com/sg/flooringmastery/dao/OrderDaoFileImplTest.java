package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoFileImplTest {
    OrderDao testOrderDao;

    public OrderDaoFileImplTest() {}

    @BeforeEach
    public void setup() throws Exception {
        // This method runs before each test method
        // Delete the existing "Tests" folder and create a new empty one before each test
        File testsFolder = new File("Tests");
        FileUtils.deleteDirectory(testsFolder);
        testsFolder.mkdir();

        testOrderDao = new OrderDaoFileImpl("Tests");
    }

    @Test
    public void testAddGetOrder() throws Exception {
        //Creating input test
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

        //get order should throw NoSuchOrderException because order is not added
        try {
            // Call the method that should throw an exception
            testOrderDao.getOrder(orderDate, orderNumber);
            // If the method doesn't throw an exception, fail the test
            fail("Expected NoSuchOrderExcpetion to be thrown, but no exception was thrown");
        } catch (NoSuchOrderException e) {
            return;
        }

        Order order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                costPerSquareFoot, labourCostPerSquareFoot, materialCost, area, labourCost, tax, total);

        //add the Order to the dao
        testOrderDao.addOrder(order);
        // get the Order from the dao
        Order retrievedOrder = testOrderDao.getOrder(orderDate, orderNumber);

        //check the data is equal
        assertEquals(order.getOrderNumber(), retrievedOrder.getOrderNumber(), "Checking Order ID");

        assertEquals(order.getCustomerName(), retrievedOrder.getCustomerName(), "Checking Order Customer Name");

        assertEquals(order.getArea(), retrievedOrder.getArea(), "Checking Order Area");

        assertEquals(order.getTotal(), retrievedOrder.getTotal(), "Checking Order Total");
    }

    @Test
    public void testGetOrdersForDate() throws Exception {
        // adding 2 orders for the date: 06/06/2024
        // adding 1 order for the date: 06/07/2024

        //Creating input test
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

        //Creating input test
        int orderNumber2 = 2;
        String customerName2 = "Bilal2";
        String state2 = "CA";
        LocalDate orderDate2 = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate2 = new BigDecimal("25.00");
        String productType2 = "Tile";
        BigDecimal costPerSquareFoot2 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot2 = new BigDecimal("4.15");
        BigDecimal materialCost2 = new BigDecimal("871.50");
        BigDecimal area2 = new BigDecimal("249.00");
        BigDecimal labourCost2 = new BigDecimal("1033.35");
        BigDecimal tax2 = new BigDecimal("476.21");
        BigDecimal total2 = new BigDecimal("2381.06");

        //Creating input test
        int orderNumber3 = 3;
        String customerName3 = "Bilal3";
        String state3 = "CA";
        LocalDate orderDate3 = LocalDate.parse("06/07/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate3 = new BigDecimal("25.00");
        String productType3 = "Tile";
        BigDecimal costPerSquareFoot3 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot3 = new BigDecimal("4.15");
        BigDecimal materialCost3 = new BigDecimal("871.50");
        BigDecimal area3 = new BigDecimal("249.00");
        BigDecimal labourCost3 = new BigDecimal("1033.35");
        BigDecimal tax3 = new BigDecimal("476.21");
        BigDecimal total3 = new BigDecimal("2381.06");


        Order order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                costPerSquareFoot, labourCostPerSquareFoot, materialCost, area, labourCost, tax, total);

        Order order2 = new Order(orderNumber2, customerName2, state2, orderDate2, taxRate2, productType2,
                costPerSquareFoot2, labourCostPerSquareFoot2, materialCost2, area2, labourCost2, tax2, total2);

        Order order3 = new Order(orderNumber3, customerName3, state3, orderDate3, taxRate3, productType3,
                costPerSquareFoot3, labourCostPerSquareFoot3, materialCost3, area3, labourCost3, tax3, total3);

        //add the Orders to the dao
        testOrderDao.addOrder(order);
        testOrderDao.addOrder(order2);
        testOrderDao.addOrder(order3);

        //retrieve the list results
        List<Order> twoOrders = testOrderDao.getOrdersForDate(orderDate);
        List<Order> oneOrder = testOrderDao.getOrdersForDate(orderDate3);
        List<Order> noOrder = testOrderDao.getOrdersForDate(LocalDate.parse("06/08/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        assertEquals(twoOrders.size(), 2, "List should have two Orders");

        assertEquals(oneOrder.size(), 1, "List should have one Order");

        assertTrue(noOrder.isEmpty(), "List Should be empty");
    }

    @Test
    public void testRemoveOrder() throws Exception {
        // adding 2 orders for the date: 06/06/2024
        // adding 1 order for the date: 06/07/2024

        //Creating input test
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

        //Creating input test
        int orderNumber2 = 2;
        String customerName2 = "Bilal2";
        String state2 = "CA";
        LocalDate orderDate2 = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate2 = new BigDecimal("25.00");
        String productType2 = "Tile";
        BigDecimal costPerSquareFoot2 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot2 = new BigDecimal("4.15");
        BigDecimal materialCost2 = new BigDecimal("871.50");
        BigDecimal area2 = new BigDecimal("249.00");
        BigDecimal labourCost2 = new BigDecimal("1033.35");
        BigDecimal tax2 = new BigDecimal("476.21");
        BigDecimal total2 = new BigDecimal("2381.06");

        //Creating input test
        int orderNumber3 = 3;
        String customerName3 = "Bilal3";
        String state3 = "CA";
        LocalDate orderDate3 = LocalDate.parse("06/07/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate3 = new BigDecimal("25.00");
        String productType3 = "Tile";
        BigDecimal costPerSquareFoot3 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot3 = new BigDecimal("4.15");
        BigDecimal materialCost3 = new BigDecimal("871.50");
        BigDecimal area3 = new BigDecimal("249.00");
        BigDecimal labourCost3 = new BigDecimal("1033.35");
        BigDecimal tax3 = new BigDecimal("476.21");
        BigDecimal total3 = new BigDecimal("2381.06");


        Order order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                costPerSquareFoot, labourCostPerSquareFoot, materialCost, area, labourCost, tax, total);

        Order order2 = new Order(orderNumber2, customerName2, state2, orderDate2, taxRate2, productType2,
                costPerSquareFoot2, labourCostPerSquareFoot2, materialCost2, area2, labourCost2, tax2, total2);

        Order order3 = new Order(orderNumber3, customerName3, state3, orderDate3, taxRate3, productType3,
                costPerSquareFoot3, labourCostPerSquareFoot3, materialCost3, area3, labourCost3, tax3, total3);

        //add the Orders to the dao
        testOrderDao.addOrder(order);
        testOrderDao.addOrder(order2);
        testOrderDao.addOrder(order3);

        //order with id 1 should be available before removing it
        Order retrievedOrder = testOrderDao.getOrder(order.getOrderDate(), order.getOrderNumber());
        //check the data is equal
        assertEquals(order.getOrderNumber(), retrievedOrder.getOrderNumber(), "Checking Order is available before removing it");

        //order with id 1 should not be available after removing it
        testOrderDao.removeOrder(order.getOrderDate(), order.getOrderNumber());
        //get order should throw NoSuchOrderException because order id 1 is removed
        try {
            // Call the method that should throw an exception
            testOrderDao.getOrder(orderDate, orderNumber);
            // If the method doesn't throw an exception, fail the test
            fail("Expected NoSuchOrderException to be thrown, but no exception was thrown");
        } catch (NoSuchOrderException e) {
            return;
        }

        //checking if other orders are still available
        Order retrievedOrder2 = testOrderDao.getOrder(order2.getOrderDate(), order2.getOrderNumber());
        Order retrievedOrder3 = testOrderDao.getOrder(order.getOrderDate(), order.getOrderNumber());
        assertEquals(2, retrievedOrder2.getOrderNumber(), "Other orders should be available.");
        assertEquals(3, retrievedOrder3.getOrderNumber(), "Other orders should be available.");

    }

    @Test
    public void testEditOrder () throws Exception {
        // adding 2 orders for the date: 06/06/2024
        // adding 1 order for the date: 06/07/2024

        //Creating input test
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

        //Creating input test
        int orderNumber2 = 2;
        String customerName2 = "Bilal2";
        String state2 = "CA";
        LocalDate orderDate2 = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate2 = new BigDecimal("25.00");
        String productType2 = "Tile";
        BigDecimal costPerSquareFoot2 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot2 = new BigDecimal("4.15");
        BigDecimal materialCost2 = new BigDecimal("871.50");
        BigDecimal area2 = new BigDecimal("249.00");
        BigDecimal labourCost2 = new BigDecimal("1033.35");
        BigDecimal tax2 = new BigDecimal("476.21");
        BigDecimal total2 = new BigDecimal("2381.06");

        //Creating input test
        int orderNumber3 = 3;
        String customerName3 = "Bilal3";
        String state3 = "CA";
        LocalDate orderDate3 = LocalDate.parse("06/07/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate3 = new BigDecimal("25.00");
        String productType3 = "Tile";
        BigDecimal costPerSquareFoot3 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot3 = new BigDecimal("4.15");
        BigDecimal materialCost3 = new BigDecimal("871.50");
        BigDecimal area3 = new BigDecimal("249.00");
        BigDecimal labourCost3 = new BigDecimal("1033.35");
        BigDecimal tax3 = new BigDecimal("476.21");
        BigDecimal total3 = new BigDecimal("2381.06");


        Order order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                costPerSquareFoot, labourCostPerSquareFoot, materialCost, area, labourCost, tax, total);

        Order order2 = new Order(orderNumber2, customerName2, state2, orderDate2, taxRate2, productType2,
                costPerSquareFoot2, labourCostPerSquareFoot2, materialCost2, area2, labourCost2, tax2, total2);

        Order order3 = new Order(orderNumber3, customerName3, state3, orderDate3, taxRate3, productType3,
                costPerSquareFoot3, labourCostPerSquareFoot3, materialCost3, area3, labourCost3, tax3, total3);

        //add the Orders to the dao
        testOrderDao.addOrder(order);
        testOrderDao.addOrder(order2);
        testOrderDao.addOrder(order3);

        //changing the customer name for order2
        order2.setCustomerName("EditedName");
        //saving the changes
        testOrderDao.editOrder(order2);
        //retrieving order2 and checking if the name change was saved
        Order retrievedOrder2 = testOrderDao.getOrder(order2.getOrderDate(), order2.getOrderNumber());
        assertEquals("EditedName", retrievedOrder2.getCustomerName(), "Customer name should have been changed.");

    }

    @Test
    public void testGetNextOrderNumber() throws Exception {
        // adding 1 orders for the date: 06/06/2024
        // adding 1 order for the date: 06/07/2024

        //Creating input test
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

        //Creating input test
        int orderNumber3 = 3;
        String customerName3 = "Bilal3";
        String state3 = "CA";
        LocalDate orderDate3 = LocalDate.parse("06/07/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate3 = new BigDecimal("25.00");
        String productType3 = "Tile";
        BigDecimal costPerSquareFoot3 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot3 = new BigDecimal("4.15");
        BigDecimal materialCost3 = new BigDecimal("871.50");
        BigDecimal area3 = new BigDecimal("249.00");
        BigDecimal labourCost3 = new BigDecimal("1033.35");
        BigDecimal tax3 = new BigDecimal("476.21");
        BigDecimal total3 = new BigDecimal("2381.06");


        Order order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                costPerSquareFoot, labourCostPerSquareFoot, materialCost, area, labourCost, tax, total);

        Order order3 = new Order(orderNumber3, customerName3, state3, orderDate3, taxRate3, productType3,
                costPerSquareFoot3, labourCostPerSquareFoot3, materialCost3, area3, labourCost3, tax3, total3);

        //add the Orders to the dao
        testOrderDao.addOrder(order);
        testOrderDao.addOrder(order3);

        int nextOrderNumber = testOrderDao.getNextOrderNumber();
        assertEquals(4, nextOrderNumber, "We added 2 orders with id 1,and 3. Next order number should be 4");

    }

    @Test
    public void testGetAllOrders () throws Exception {
        // adding 2 orders for the date: 06/06/2024
        // adding 1 order for the date: 06/07/2024

        //Creating input test
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

        //Creating input test
        int orderNumber2 = 2;
        String customerName2 = "Bilal2";
        String state2 = "CA";
        LocalDate orderDate2 = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate2 = new BigDecimal("25.00");
        String productType2 = "Tile";
        BigDecimal costPerSquareFoot2 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot2 = new BigDecimal("4.15");
        BigDecimal materialCost2 = new BigDecimal("871.50");
        BigDecimal area2 = new BigDecimal("249.00");
        BigDecimal labourCost2 = new BigDecimal("1033.35");
        BigDecimal tax2 = new BigDecimal("476.21");
        BigDecimal total2 = new BigDecimal("2381.06");

        //Creating input test
        int orderNumber3 = 3;
        String customerName3 = "Bilal3";
        String state3 = "CA";
        LocalDate orderDate3 = LocalDate.parse("06/07/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        BigDecimal taxRate3 = new BigDecimal("25.00");
        String productType3 = "Tile";
        BigDecimal costPerSquareFoot3 = new BigDecimal("3.50");
        BigDecimal labourCostPerSquareFoot3 = new BigDecimal("4.15");
        BigDecimal materialCost3 = new BigDecimal("871.50");
        BigDecimal area3 = new BigDecimal("249.00");
        BigDecimal labourCost3 = new BigDecimal("1033.35");
        BigDecimal tax3 = new BigDecimal("476.21");
        BigDecimal total3 = new BigDecimal("2381.06");


        Order order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                costPerSquareFoot, labourCostPerSquareFoot, materialCost, area, labourCost, tax, total);

        Order order2 = new Order(orderNumber2, customerName2, state2, orderDate2, taxRate2, productType2,
                costPerSquareFoot2, labourCostPerSquareFoot2, materialCost2, area2, labourCost2, tax2, total2);

        Order order3 = new Order(orderNumber3, customerName3, state3, orderDate3, taxRate3, productType3,
                costPerSquareFoot3, labourCostPerSquareFoot3, materialCost3, area3, labourCost3, tax3, total3);

        //add the Orders to the dao
        testOrderDao.addOrder(order);
        testOrderDao.addOrder(order2);
        testOrderDao.addOrder(order3);

        Map<LocalDate,Map<Integer, Order>> orders = testOrderDao.getAllOrders();

        // for the date 06/06/2024, there should be 2 orders
        int ordersForFirstDate = orders.get(order.getOrderDate()).size();
        assertEquals(2, ordersForFirstDate, "There should be two orders for the date 06/06/2024");

        // for the date 06/07/2024, there should be 1 order
        int ordersForSecondDate = orders.get(order3.getOrderDate()).size();
        assertEquals(1, ordersForSecondDate, "There should be one order for the date 06/07/2024");
    }









}
