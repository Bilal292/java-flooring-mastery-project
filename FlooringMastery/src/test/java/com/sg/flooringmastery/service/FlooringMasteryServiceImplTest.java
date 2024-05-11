package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.NoSuchOrderException;
import com.sg.flooringmastery.dao.PersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlooringMasteryServiceImplTest {

    private FlooringMasteryService service;

    public FlooringMasteryServiceImplTest () {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        service =
                ctx.getBean("serviceLayer", FlooringMasteryService.class);
    }

    @Test
    public void testGetOrdersForDate () throws Exception {

        LocalDate orderDate = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<Order> orders = service.getOrdersForDate(orderDate);

        assertEquals(1, orders.size(), "Should be 1 order in the list");
    }

    @Test
    public void testIsValidName () {
        boolean isTrue = service.isValidName("testingValid.");
        boolean isFalse = service.isValidName("/';][sd");

        assertTrue(isTrue, "Valid name should be true");
        assertFalse(isFalse,"Invalid name should be false" );
    }

    @Test
    public void testIfFutureDate () {
        LocalDate futureDate = LocalDate.parse("06/06/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        boolean isFutureDateTrue = service.isFutureDate(futureDate);

        LocalDate notFutureDate = LocalDate.parse("06/06/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        boolean isFutureDateFalse = service.isFutureDate(notFutureDate);

        assertTrue(isFutureDateTrue, "The date is in future from now ");
        assertFalse(isFutureDateFalse, "The date is not in future");
    }

    @Test
    public void testIsValidArea () {
        boolean notValidArea1 = service.isValidArea("-1");
        boolean notValidArea2 = service.isValidArea("99");

        boolean isValidArea1= service.isValidArea("100");
        boolean isValidArea2 = service.isValidArea("200");

        assertFalse(notValidArea1, "Not positive area is not valid");
        assertFalse(notValidArea2, "less than 100 is not valid");

        assertTrue(isValidArea1,"Minimum 100 area is valid");
        assertTrue(isValidArea2,"Area above 100 is valid");
    }

    @Test
    public void testIsValidStateName () throws Exception{
        boolean isValidStateName = service.isValidStateName("CA");
        boolean notValidStateName = service.isValidStateName("NY");

        assertFalse(notValidStateName, "NY is not in TestData/Taxes.txt");
        assertTrue(isValidStateName,"CA is in TestData/Taxes.txt");
    }

    @Test
    public void testGetProducts () throws Exception {
        List<Product> products = service.getProducts();

        assertEquals(4, products.size(), "TestDate/Products.txt has 4 products");
    }

    @Test
    public void testGetNextOrderNumber () throws Exception {
        int nextOrderNumber = service.getNextOrderNumber();
        assertEquals(2, nextOrderNumber, "Next order number is 2.");
    }

    @Test
    public void testGetTaxRate () throws Exception {
        BigDecimal taxRateCA = service.getTaxRate("CA");
        BigDecimal taxRate = new BigDecimal("25.00");

        assertEquals(0, taxRate.compareTo(taxRateCA), "Tax Rate of CA is 25.00 in TestData/Taxes.txt");
    }

    @Test
    public void testAddOrder () {
        try{
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


            Order order3 = new Order(orderNumber3, customerName3, state3, orderDate3, taxRate3, productType3,
                    costPerSquareFoot3, labourCostPerSquareFoot3, materialCost3, area3, labourCost3, tax3, total3);

            service.addOrder(order3);
        } catch (PersistenceException e) {
            fail("should not throw Persistence Exception");
        }
    }

    @Test
    public void testGetOrder () throws Exception {
        LocalDate orderDate = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Order order = service.getOrder(orderDate, 1);

        LocalDate orderDateNull = LocalDate.parse("06/06/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Order orderNull = service.getOrder(orderDateNull, 2);

        assertEquals(1, order.getOrderNumber(), "The order exists so it should be returned");

        assertNull(orderNull, "Order does not exists");
    }

    @Test
    public void testEditOrder () {
        try{
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


            Order order3 = new Order(orderNumber3, customerName3, state3, orderDate3, taxRate3, productType3,
                    costPerSquareFoot3, labourCostPerSquareFoot3, materialCost3, area3, labourCost3, tax3, total3);

            service.editOrder(order3);
        } catch (PersistenceException | NoSuchOrderException e) {
            fail("should not throw Persistence/NoSuchOrder Exception");
        }
    }

    @Test
    public void testRemoveOrder () {
        try{
            LocalDate orderDate = LocalDate.parse("06/06/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            service.removeOrder(orderDate, 1);
        } catch (PersistenceException | NoSuchOrderException e) {
            fail("should not throw Persistence/NoSuchOrder Exception");
        }
    }

    public void testExportData () {
        try{
           service.exportData();
        } catch (PersistenceException e) {
            fail("should not throw Persistence Exception");
        }
    }

}
