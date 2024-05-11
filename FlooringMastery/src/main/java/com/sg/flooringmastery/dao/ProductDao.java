package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;

import java.util.List;

public interface ProductDao {
    void loadFile() throws PersistenceException;

    List<Product> getAllProducts();
}
