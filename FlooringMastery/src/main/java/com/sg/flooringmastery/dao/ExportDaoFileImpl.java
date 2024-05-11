package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportDaoFileImpl implements ExportDao{
    public static final String DELIMITER = ",";
    private final String BACKUP_FOLDER;

    private List<Order> allOrders = new ArrayList<>();

    public ExportDaoFileImpl() {
        this.BACKUP_FOLDER = "Backup";
    }

    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> orders) throws PersistenceException {
        for(Map<Integer, Order> mapOrder : orders.values() ){
            for (Order order : mapOrder.values()) {
                allOrders.add(order);
            }
        }
        this.writeFile();
    }

    public void writeFile() throws PersistenceException {
        //represents a file path to the backup data
        File file = new File(BACKUP_FOLDER + "/DataExport.txt");

        //temporary file path to export the data
        File tempFile = new File(BACKUP_FOLDER + "/temp.txt");

        try {
            tempFile.createNewFile();

            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            //the first line of the temp file should be the headers
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                    "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");

            for (Order order : allOrders) {
                String orderString = this.orderToString(order);
                writer.println(orderString);
            }

            // Replace the original file with the temporary file
            file.delete();
            tempFile.renameTo(file);

            writer.close();

        } catch (IOException e) {
            throw new PersistenceException("Could not Export Data.", e);
        }

    }

    public String orderToString(Order order){
        return order.getOrderNumber() + DELIMITER + order.getCustomerName() + DELIMITER
                + order.getState() + DELIMITER + order.getTaxRate() + DELIMITER + order.getProductType() + DELIMITER
                + order.getArea() + DELIMITER + order.getCostPerSquareFoot() + DELIMITER + order.getLabourCostPerSquareFoot()
                + DELIMITER + order.getMaterialCost() + DELIMITER + order.getLabourCost() + DELIMITER + order.getTax()
                + DELIMITER + order.getTotal() + DELIMITER + order.getOrderDate();
    }


}
