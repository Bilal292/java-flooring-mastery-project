package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.NoSuchOrderException;
import com.sg.flooringmastery.dao.PersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.service.FlooringMasteryService;
import com.sg.flooringmastery.view.FlooringMasteryView;
import com.sg.flooringmastery.view.UserIO;
import com.sg.flooringmastery.view.UserIOConsoleImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

    private UserIO io = new UserIOConsoleImpl();
    private FlooringMasteryView view;
    private FlooringMasteryService service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryService service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        System.out.println("not implemented");
                }

            }
            exitMessage();
        } catch (PersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (NumberFormatException e) {
            view.displayErrorInvalidSelection();
            this.run();
        }
    }

    private void displayOrders() throws PersistenceException {
        LocalDate date = view.getDateInput();
        List<Order> orderList = service.getOrdersForDate(date);

        view.displayOrders(orderList);
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    private void exitMessage() {
        view.displayExitBanner();
    }

    public void addOrder() throws PersistenceException{
        view.displayCreateOrderBanner();
        LocalDate orderDate;
        String customerName;
        String stateName;
        int productSelection;
        BigDecimal area;

        // get valid date in future
        while(true){
             orderDate = view.getDateInput();
            if(service.isFutureDate(orderDate)) {
                break;
            }else {
                view.displayErrorMessage("Enter a Valid date in Future from Now");
            }
        }

        // get valid customer name
        while(true){
            customerName = view.getStringInput("Please Enter Valid Name for the Order ([a-z][0-9] as well as periods and comma characters)");
            if(service.isValidName(customerName)) {
                break;
            }else{
                view.displayErrorMessage("Name not Valid, Try Again");
            }
        }

        // get a valid state name
        while(true){
            stateName = view.getStringInput("Please Enter State abbreviated Name");
            if(service.isValidStateName(stateName)) {
                break;
            }else{
                view.displayErrorMessage("Sorry, we do not sell in that State");
            }
        }

        // get a valid product input
        List<Product> products = service.getProducts();
        productSelection = view.listProducts(products); // lists out the products and gets user selection as an int


        // get a valid area
        while(true){
            String userArea = view.getStringInput("Please Enter Area per Square Foot");
            if(service.isValidArea(userArea)) {
                area = new BigDecimal(userArea);
                break;
            }else{
                view.displayErrorMessage("The area must be a positive decimal. Minimum order size is 100 sq ft");
            }
        }

        // create the Order subject using the user inputs

        int orderNumber = service.getNextOrderNumber();
        Order order = new Order(orderNumber, customerName, stateName, orderDate,
                products.get(productSelection-1).getProductType(), area);

        order.setCostPerSquareFoot(products.get(productSelection-1).getCostPerSquareFoot());
        order.setLabourCostPerSquareFoot(products.get(productSelection-1).getLaborCostPerSquareFoot());

        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());
        order.setMaterialCost(materialCost);

        BigDecimal laborCost = order.getArea().multiply(order.getLabourCostPerSquareFoot());
        order.setLabourCost(laborCost);

        BigDecimal taxRate = service.getTaxRate(stateName);
        order.setTaxRate(taxRate);

        //calculate tax
        BigDecimal tax = service.calculateTax(order);
        order.setTax(tax);

        BigDecimal total = order.getLabourCost().add(order.getMaterialCost());
        order.setTotal(total.add(order.getTax()));

        view.displayOrderInfo(order);

        // add the order or not
        String add = view.getStringInput("Do you want to add this Order? Y/N");
        if (add.equals("Y")){
            service.addOrder(order);
            view.displayAddOrderSuccessBanner();
        } else {
            view.getStringInput("Order will not be saved. Please hit enter to continue");
        }

    }

    public void editOrder() throws PersistenceException{
        LocalDate orderDate;
        int orderNumber;
        Order orderToEdit;

        // get valid order date and number
        orderDate = view.getDateInput();
        orderNumber = view.getOrderNumberInput();

        try{
            orderToEdit = service.getOrder(orderDate, orderNumber);
            boolean recalculateOrder = false;

            // new customer name
            while(true) {
                String newCustomerName = view.getStringInput("Current Customer Name: "
                        + orderToEdit.getCustomerName() + ". Enter new Customer name or hit Enter to keep it the same: " );

                // if user gave a new customer name then check if its valid
                if (!newCustomerName.isEmpty()) {
                    if(service.isValidName(newCustomerName)) {
                        orderToEdit.setCustomerName(newCustomerName); // change the customer name
                        break;
                    }else{
                        view.displayErrorMessage("Name not Valid, Try Again");
                    }
                } else {
                    break;
                }
            }

            // new order state
            while(true) {
                String newState = view.getStringInput("Current Order State is: "
                        + orderToEdit.getState() + ". Enter new Order State Name or hit Enter to keep it the same: " );

                // if user gave a new customer name then check if its valid
                if (!newState.isEmpty()) {
                    if(service.isValidStateName(newState)) {
                        recalculateOrder = true;
                        orderToEdit.setState(newState); // change the customer name

                        BigDecimal taxRate = service.getTaxRate(newState);
                        orderToEdit.setTaxRate(taxRate);
                        break;
                    }else{
                        view.displayErrorMessage("Sorry, we do not sell in that State");
                    }
                } else {
                    break;
                }
            }

            // new product type
            List<Product> products = service.getProducts();
            view.displayMessage("Current Product Type is: " + orderToEdit.getProductType() + ".");
            String changeProduct = view.getStringInput("Please hit Enter to keep it the same, or type Y to change it: ");
            if(changeProduct.equals("Y")) {
                recalculateOrder = true;
                int newProductSelection = view.listProducts(products); // lists out the products and gets user selection as an int
                orderToEdit.setProductType(products.get(newProductSelection-1).getProductType());

                orderToEdit.setCostPerSquareFoot(products.get(newProductSelection-1).getCostPerSquareFoot());
                orderToEdit.setLabourCostPerSquareFoot(products.get(newProductSelection-1).getLaborCostPerSquareFoot());
            }

            // get new area
            while(true) {
                String newArea = view.getStringInput("Current Order Area is: "
                        + orderToEdit.getArea() + ". Enter new Order Area, or hit Enter to keep it the same: " );

                // if user gave a new area, check if its valid
                if (!newArea.isEmpty()) {
                    if(service.isValidArea(newArea)) {
                        recalculateOrder = true;
                        BigDecimal area = new BigDecimal(newArea);
                        orderToEdit.setArea(area); // change the customer name
                        break;
                    }else{
                        view.displayErrorMessage("The area must be a positive decimal. Minimum order size is 100 sq ft");
                    }
                } else {
                    break;
                }
            }

            //check if order needs to be recalculated
            if(recalculateOrder){
                BigDecimal materialCost = orderToEdit.getArea().multiply(orderToEdit.getCostPerSquareFoot());
                orderToEdit.setMaterialCost(materialCost);

                BigDecimal laborCost = orderToEdit.getArea().multiply(orderToEdit.getLabourCostPerSquareFoot());
                orderToEdit.setLabourCost(laborCost);

                //calculate tax
                BigDecimal tax = service.calculateTax(orderToEdit);
                orderToEdit.setTax(tax);

                BigDecimal total = orderToEdit.getLabourCost().add(orderToEdit.getMaterialCost());
                orderToEdit.setTotal(total.add(orderToEdit.getTax()));
            }

            view.displayOrderInfo(orderToEdit);
            String saveEditOrder = view.getStringInput("Do you want to save the Following Order Edits (Y/N):");

            if(saveEditOrder.equals("Y")){
                service.editOrder(orderToEdit);
                view.getStringInput("Order Edit Saved. Please hit enter to continue");
            } else{
                view.getStringInput("Order Edit NOT Saved. Please hit enter to continue");
            }

        } catch (NoSuchOrderException e){
            view.displayErrorMessage(e.getMessage());
            view.getStringInput("Please hit enter to continue");
        }

    }

    public void removeOrder() throws PersistenceException{

        try{

            LocalDate orderDate = view.getDateInput();
            int orderNumber = view.getOrderNumberInput();

            Order order = service.getOrder(orderDate, orderNumber);

            view.displayOrderInfo(order);
            String confirmDelete = view.getStringInput("Do you want to delete this Order? (Y/N): ");

            if(confirmDelete.equals("Y")){
                service.removeOrder(orderDate, orderNumber);
                view.getStringInput("Order Removed. Please hit enter to continue");
            } else {
                view.getStringInput("Order Not Removed. Please hit enter to continue");
            }


        } catch (NoSuchOrderException e) {
            view.displayErrorMessage(e.getMessage());
            view.getStringInput("Please hit enter to continue");
        }


    }

    public void exportData() throws PersistenceException {
        service.exportData();
        view.getStringInput("Export Data Complete. Please hit enter to continue");
    }


}
