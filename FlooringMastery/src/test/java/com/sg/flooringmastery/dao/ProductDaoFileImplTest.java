package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoFileImplTest {
    ProductDao testProductDao;

    public void ProductDaoFileImplTest () {}

    @BeforeEach
    public void setUp() {
        testProductDao = new ProductDaoFileImpl("TestData/Products.txt");
    }

    @Test
    public void testGetAllProducts () throws Exception {
        testProductDao.loadFile();
        List<Product> products = testProductDao.getAllProducts();

        int numProducts = products.size();
        assertEquals(4, numProducts, "There should be 4 products in TestData/Products.txt file");

        Product product = products.get(0);
        assertEquals("Carpet", product.getProductType(), "Product type at index 0 should be Carpet ");
    }

}
