package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.Map;

public class ExportDaoStubImpl implements ExportDao{
    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> orders) throws PersistenceException {
        //do nothing...
    }
}
