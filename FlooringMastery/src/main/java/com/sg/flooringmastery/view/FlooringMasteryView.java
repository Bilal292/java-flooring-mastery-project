package com.sg.flooringmastery.view;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("Main Menu");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public LocalDate getDateInput(){
        String date = io.readString("Please Enter the Order Date (DD/MM/YYYY)");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate orderDate = LocalDate.parse(date, formatter);
            return orderDate;
        } catch (DateTimeParseException e) {
            io.print("Invalid date format. Please enter the date in DD/MM/YYYY format.");
            return getDateInput(); // recursive call
        }

    }

    public String getStringInput(String prompt){
        return io.readString(prompt);
    }

    public void displayOrders(List<Order> orders) {
        if (orders.size() > 0) {
            for (Order order: orders) {
                this.displayOrderInfo(order);
            }
        } else {
            io.print("=== No Orders Found for Given Date ===");
        }

        io.readString("Please hit enter to continue.");

    }

    public void displayOrderInfo(Order order) {
        System.out.println(order);
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }
    public void displayErrorInvalidSelection() {
        io.print("=== Select a Valid Option from the Menu ===");
    }

    public void displayCreateOrderBanner(){
        io.print("=== Create Order ===");
    }

    public int listProducts(List<Product> products) {
        int iteration = 1;
        for (Product product : products){
            io.print(iteration + ". " + product.toString());
            iteration++;
        }

        return io.readInt("Please select from the above choices.", 1, products.size());

    }

    public void displayAddOrderSuccessBanner() {
        io.readString("Order successfully Added.  Please hit enter to continue");
    }

    public void displayMessage(String msg){
        io.print(msg);
    }
    public int getOrderNumberInput(){
        return io.readInt("Please Enter the Order Number");
    }



}
