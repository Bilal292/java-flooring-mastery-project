package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class TaxDaoStubImpl implements TaxDao {
    public static final String DELIMITER = ",";
    private final String TAX_FILE;

    Map<String, Tax> allTaxes = new LinkedHashMap<>();

    public TaxDaoStubImpl() {
        this.TAX_FILE = "TestData/Taxes.txt";
    }

    @Override
    public void loadFile() throws PersistenceException {
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
            scanner.nextLine(); //skip the header line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(DELIMITER);

                //check the file is in correct format
                if (parts.length == 3) {
                    String state = parts[0];
                    String stateName = parts[1];
                    BigDecimal taxRate = new BigDecimal(parts[2]);

                    Tax tax = new Tax(stateName, state, taxRate);
                    allTaxes.put(state, tax);
                } else {
                    throw new PersistenceException("Invalid data format in file");
                }
            }
        } catch (FileNotFoundException e) {
            throw new PersistenceException("-_- Could not load Tax data into memory.", e);
        }

    }

    @Override
    public List<Tax> getAllTaxes() {
        return new ArrayList<>(allTaxes.values());
    }

    @Override
    public boolean stateInFile(String state) {
        return allTaxes.containsKey(state);
    }

    @Override
    public BigDecimal getTaxForState(String name) throws PersistenceException {
        this.loadFile();
        Tax tax = allTaxes.get(name);
        return tax.getTaxRate();
    }
}
