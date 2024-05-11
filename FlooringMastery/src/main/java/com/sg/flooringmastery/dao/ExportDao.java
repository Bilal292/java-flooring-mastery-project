package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.Map;

public interface ExportDao {
    void exportData(Map<LocalDate,Map<Integer, Order>> orders) throws PersistenceException;

}
