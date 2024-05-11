package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaxDaoFileImplTest {
    TaxDao testTaxDao;

    public TaxDaoFileImplTest () {}

    @BeforeEach
    public void setUp () {
        this.testTaxDao = new TaxDaoFileImpl("TestData/Taxes.txt");
    }

    @Test
    public void testGetALlTaxes () throws Exception {
        testTaxDao.loadFile();
        List<Tax> taxes = testTaxDao.getAllTaxes();

        int numTaxes = taxes.size();
        assertEquals(4, numTaxes, "There should be 4 Taxes info in TestData/Taxes.txt file");
    }

    @Test
    public void testStateInFile () throws Exception{
        testTaxDao.loadFile();

        String taxInFile = "CA";
        String taxNotInFile = "NY";

        //should be true
        boolean isTrue = testTaxDao.stateInFile(taxInFile);

        //should be false
        boolean isFalse = testTaxDao.stateInFile(taxNotInFile);

        assertTrue(isTrue, "CA is in Tax File");
        assertFalse(isFalse, "NY is not in Tax File");
    }

    @Test
    public void testGetTaxForState () throws  Exception {
        testTaxDao.loadFile();

        BigDecimal taxOfCA = new BigDecimal("25.00");

        BigDecimal retrievedTaxOfCA = testTaxDao.getTaxForState("CA");

        assertEquals(0, taxOfCA.compareTo(retrievedTaxOfCA), "Tax of CA should be 25.00 in the File");

    }

}
