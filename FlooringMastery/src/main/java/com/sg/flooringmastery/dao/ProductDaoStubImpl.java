package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoStubImpl implements ProductDao{
    public static final String DELIMITER = ",";
    private final String PRODUCT_FILE;

    Map<String, Product> allProducts = new LinkedHashMap<>();

    public ProductDaoStubImpl(){
        this.PRODUCT_FILE = "TestData/Products.txt";
    }

    @Override
    public void loadFile() throws PersistenceException {
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
            scanner.nextLine(); //skip the header line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(DELIMITER);

                //check the file is in correct format
                if (parts.length == 3) {
                    String productType = parts[0];
                    BigDecimal costPerSquareFoot = new BigDecimal(parts[1]);
                    BigDecimal laborCostPerSquareFoot = new BigDecimal(parts[2]);

                    Product product = new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
                    allProducts.put(productType, product);
                } else {
                    throw new PersistenceException("Invalid data format in file");
                }
            }
        } catch (FileNotFoundException e) {
            throw new PersistenceException("-_- Could not load Tax data into memory.", e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(allProducts.values());
    }
}
