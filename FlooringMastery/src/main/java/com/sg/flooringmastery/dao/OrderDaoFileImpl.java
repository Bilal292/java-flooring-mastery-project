package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDaoFileImpl implements OrderDao{
    public static final String DELIMITER = ",";
    private final String ORDER_FOLDER;

    private Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();

    public OrderDaoFileImpl() {
        this.ORDER_FOLDER = "Orders";
    }
    public OrderDaoFileImpl(String fileName) {
        this.ORDER_FOLDER = fileName;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws PersistenceException {
        /*
        Load the data from the Files first
        Get the HashMap for the given date
        Extract the values and return them in an ArrayList
        */
        this.load();

        Map<Integer, Order> ordersForDate = orders.get(date);
        if (ordersForDate != null) {
            return new ArrayList<>(ordersForDate.values());
        } else {
            return new ArrayList<>();
        }

    }


    public void load() throws PersistenceException{
        try{
            List<String> fileNames = Files.list(Paths.get(ORDER_FOLDER)) //takes the folder name and lists all the files in the given directory and creates path objects
                    .map(path -> path.getFileName().toString()) // for each path object, get the file name as string
                    .filter(name -> !name.equals(".DS_Store")) //.DS_Store is a hidden file created by macOS Finder, it should be excluded
                    .collect(Collectors.toList()); //collects the elements of stream, so we have names of all the files

            for (String fileName : fileNames) {
                LocalDate date = this.parseDateFromFileName(fileName); //method to extract the date from the file name

                Map<Integer, Order> ordersForDate = new HashMap<>();

                List<String> lines = Files.readAllLines(Paths.get(ORDER_FOLDER, fileName));
                for (String line : lines.subList(1, lines.size())) { //skips the first containing column names
                    String[] parts = line.split(DELIMITER);

                    int orderNumber = Integer.parseInt(parts[0]);
                    String customerName = parts[1];
                    String state = parts[2];
                    BigDecimal taxRate = new BigDecimal(parts[3]);
                    String productType = parts[4];
                    BigDecimal area = new BigDecimal(parts[5]);
                    BigDecimal costPerSquareFoot = new BigDecimal(parts[6]);
                    BigDecimal laborCostPerSquareFoot = new BigDecimal(parts[7]);
                    BigDecimal materialCost = new BigDecimal(parts[8]);
                    BigDecimal laborCost = new BigDecimal(parts[9]);
                    BigDecimal tax = new BigDecimal(parts[10]);
                    BigDecimal total = new BigDecimal(parts[11]);

                    Order order = new Order(orderNumber, customerName, state, date, taxRate, productType,
                            costPerSquareFoot, laborCostPerSquareFoot, materialCost, area, laborCost, tax, total);

                    ordersForDate.put(orderNumber, order);
                }

                orders.put(date, ordersForDate);
            }
        } catch (IOException e) {
            throw new PersistenceException("-_- Could not load Orders data into memory from the Folder.", e);
        }

    }

    public LocalDate parseDateFromFileName(String fileName){
        // to get only the date from file name e.g. "Orders_06012013.txt", dateToStr will be "06012013"
        String fileDate = fileName.substring(fileName.indexOf("_")+1, fileName.indexOf(".txt"));

        int year = Integer.parseInt(fileDate.substring(4));
        int month = Integer.parseInt((fileDate.substring(2, 4)));
        int day = Integer.parseInt(fileDate.substring(0,2));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date =  LocalDate.of(year, month, day);
        String dateToStr = String.format("%02d/%02d/%04d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        LocalDate formattedDate = LocalDate.parse(dateToStr, formatter);

        return formattedDate;
    }

    @Override
    public int getNextOrderNumber() throws PersistenceException {
        this.load();
        int nextOrderNumber = 1;

        //goes through all the orders for each date
        for (Map<Integer, Order> orderMap : orders.values()) {
            // if a map for a give order date is  not empty
            if (!orderMap.isEmpty()) {
                //find the maximum order number for the current date

                int maxOrderNumber = orderMap.keySet().stream() //turn the keySet to stream to perform functional operations
                        .mapToInt(Integer::intValue).max() // map the Integer keys to primitive int type because max function only works on primitives
                        .orElse(0); //orElse returns 0 if there are no order numbers

                //check which one is larger - that will be the next order number
                nextOrderNumber = Math.max(nextOrderNumber, maxOrderNumber + 1);
            }
        }

        return nextOrderNumber;
    }

    @Override
    public void addOrder(Order order) throws PersistenceException {
        //creating the name of the file in which the order should be in
        String fileName = "Orders_" + order.getOrderDate().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".txt";
        //represents a file path
        File file = new File(ORDER_FOLDER + "/" + fileName);

        //create a new file if the file path doesn't exist already
        if (!file.exists()) {
            try {
                file.createNewFile();

                //the first line of the file should be the headers
                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
                }
            } catch (IOException e) {
                throw new PersistenceException("Could not save order data in new file.", e);
            }
        }

        //Append the order to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            String orderString = this.orderToString(order);

            writer.println(orderString);

        } catch (IOException e) {
            throw new PersistenceException("Could not save order data in the file.", e);
        }

    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException {
        //creating the name of the file in which the order should be in
        String fileName = "Orders_" + orderDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".txt";
        //represents a file path
        File file = new File(ORDER_FOLDER + "/" + fileName);

        //check if the file exists
        if (!file.exists()){
            throw new NoSuchOrderException("No orders found for the specified date: " + orderDate);
        }


        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();

                //skipping the header line
                if (line.startsWith("OrderNumber")) {
                    continue;
                }

                String[] parts = line.split(DELIMITER);
                int currentLineOrderNumber = Integer.parseInt(parts[0]);

                // if order number is found then create the order object and return it
                if (currentLineOrderNumber == orderNumber) {

                    String customerName = parts[1];
                    String state = parts[2];
                    BigDecimal taxRate = new BigDecimal(parts[3]);
                    String productType = parts[4];
                    BigDecimal area = new BigDecimal(parts[5]);
                    BigDecimal costPerSquareFoot = new BigDecimal(parts[6]);
                    BigDecimal laborCostPerSquareFoot = new BigDecimal(parts[7]);
                    BigDecimal materialCost = new BigDecimal(parts[8]);
                    BigDecimal laborCost = new BigDecimal(parts[9]);
                    BigDecimal tax = new BigDecimal(parts[10]);
                    BigDecimal total = new BigDecimal(parts[11]);

                    Order order = new Order(orderNumber, customerName, state, orderDate, taxRate, productType,
                            costPerSquareFoot, laborCostPerSquareFoot, materialCost, area, laborCost, tax, total);

                    return order;
                }
            }
            scanner.close();

            // Order with the specified number not found
            throw new NoSuchOrderException("Order with number " + orderNumber + " not found for the specified date: " + orderDate);

        } catch (IOException e) {
            throw new NoSuchOrderException("Error reading order file" + e);
        }

    }

    @Override
    public void editOrder(Order order) throws PersistenceException, NoSuchOrderException {
        LocalDate orderDate = order.getOrderDate();
        int orderNumber = order.getOrderNumber();

        //creating the name of the file in which the order should be in
        String fileName = "Orders_" + orderDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".txt";
        //represents a file path
        File file = new File(ORDER_FOLDER + "/" + fileName);

        //check if the file exists
        if (!file.exists()){
            throw new NoSuchOrderException("No orders found for the specified date: " + orderDate);
        }

        //temporary file to write the updated order
        File tempFile = new File(ORDER_FOLDER + "/" + "temp.txt");

        try {
            tempFile.createNewFile();

            //the first line of the temp file should be the headers
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

            // Go through the order file and
            Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //skipping the header line
                if (line.startsWith("OrderNumber")) {
                    continue;
                }

                String[] parts = line.split(DELIMITER);

                // if the order number matches, replace the line with updated order
                if (Integer.parseInt(parts[0]) == orderNumber) {
                    line = orderToString(order);
                }
                writer.println(line);
            }

            // Replace the original file with the temporary file
            file.delete();
            tempFile.renameTo(file);

            writer.close();
            scanner.close();

        } catch (IOException e) {
            throw new PersistenceException("Could not save edit order data in file.", e);
        }

    }

    @Override
    public void removeOrder(LocalDate orderDate, int orderNumber) throws PersistenceException, NoSuchOrderException {
        //creating the name of the file in which the order should be in
        String fileName = "Orders_" + orderDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".txt";
        //represents a file path
        File file = new File(ORDER_FOLDER + "/" + fileName);

        //check if the file exists
        if (!file.exists()){
            throw new NoSuchOrderException("No orders found for the specified date: " + orderDate);
        }

        //temporary file that will exclude the order to delete
        File tempFile = new File(ORDER_FOLDER + "/" + "temp.txt");

        try {
            tempFile.createNewFile();

            //the first line of the temp file should be the headers
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

            // Go through the order file and
            Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //skipping the header line
                if (line.startsWith("OrderNumber")) {
                    continue;
                }

                String[] parts = line.split(DELIMITER);

                // if the order number matches, then we dont write that order in the tempFile
                if (Integer.parseInt(parts[0]) != orderNumber) {
                    writer.println(line);
                }

            }

            // Replace the original file with the temporary file
            file.delete();
            tempFile.renameTo(file);

            writer.close();
            scanner.close();

        } catch (IOException e) {
            throw new PersistenceException("Could not delete order data in file.", e);
        }

    }

    public String orderToString(Order order){
        return order.getOrderNumber() + "," + order.getCustomerName() + ","
                + order.getState() + "," + order.getTaxRate() + "," + order.getProductType() + ","
                + order.getArea() + "," + order.getCostPerSquareFoot() + "," + order.getLabourCostPerSquareFoot()
                + "," + order.getMaterialCost() + "," + order.getLabourCost() + "," + order.getTax() + "," + order.getTotal();
    }

    @Override
    public Map<LocalDate,Map<Integer, Order>> getAllOrders() throws PersistenceException {
        this.load();
        return this.orders;
    }

}
