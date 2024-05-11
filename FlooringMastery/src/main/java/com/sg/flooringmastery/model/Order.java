package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Order {

    private int orderNumber;
    private String customerName;
    private String state;
    private LocalDate orderDate;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal labourCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal area;
    private BigDecimal labourCost;
    private BigDecimal tax;
    private BigDecimal total;

    public Order(int orderNumber, String customerName, String state, LocalDate orderDate,
                 String productType, BigDecimal area) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.orderDate = orderDate;
        this.productType = productType;
        this.area = area.setScale(2, RoundingMode.HALF_UP);
    }

    public Order(int orderNumber, String customerName, String state, LocalDate orderDate, BigDecimal taxRate,
                 String productType, BigDecimal costPerSquareFoot, BigDecimal labourCostPerSquareFoot,
                 BigDecimal materialCost, BigDecimal area, BigDecimal labourCost, BigDecimal tax, BigDecimal total) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.orderDate = orderDate;
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        this.labourCostPerSquareFoot = labourCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        this.materialCost = materialCost.setScale(2, RoundingMode.HALF_UP);
        this.area = area.setScale(2, RoundingMode.HALF_UP);
        this.labourCost = labourCost.setScale(2, RoundingMode.HALF_UP);
        this.tax = tax.setScale(2, RoundingMode.HALF_UP);
        this.total = total.setScale(2, RoundingMode.HALF_UP);
    }


    public int getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getState() {
        return state;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public BigDecimal getLabourCostPerSquareFoot() {
        return labourCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getArea() {
        return area;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public void setLabourCostPerSquareFoot(BigDecimal labourCostPerSquareFoot) {
        this.labourCostPerSquareFoot = labourCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setArea(BigDecimal area) {
        this.area = area.setScale(2, RoundingMode.HALF_UP);
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTotal(BigDecimal total) {
        this.total = total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", customerName='" + customerName + '\'' +
                ", state='" + state + '\'' +
                ", orderDate=" + orderDate +
                ", taxRate=" + taxRate +
                ", productType='" + productType + '\'' +
                ", costPerSquareFoot=" + costPerSquareFoot +
                ", labourCostPerSquareFoot=" + labourCostPerSquareFoot +
                ", materialCost=" + materialCost +
                ", area=" + area +
                ", labourCost=" + labourCost +
                ", tax=" + tax +
                ", total=" + total +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderNumber != order.orderNumber) return false;
        if (!Objects.equals(customerName, order.customerName)) return false;
        if (!Objects.equals(state, order.state)) return false;
        if (!Objects.equals(orderDate, order.orderDate)) return false;
        if (!Objects.equals(taxRate, order.taxRate)) return false;
        if (!Objects.equals(productType, order.productType)) return false;
        if (!Objects.equals(costPerSquareFoot, order.costPerSquareFoot))
            return false;
        if (!Objects.equals(labourCostPerSquareFoot, order.labourCostPerSquareFoot))
            return false;
        if (!Objects.equals(materialCost, order.materialCost)) return false;
        if (!Objects.equals(area, order.area)) return false;
        if (!Objects.equals(labourCost, order.labourCost)) return false;
        if (!Objects.equals(tax, order.tax)) return false;
        return Objects.equals(total, order.total);
    }

    @Override
    public int hashCode() {
        int result = orderNumber;
        result = 31 * result + (customerName != null ? customerName.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (taxRate != null ? taxRate.hashCode() : 0);
        result = 31 * result + (productType != null ? productType.hashCode() : 0);
        result = 31 * result + (costPerSquareFoot != null ? costPerSquareFoot.hashCode() : 0);
        result = 31 * result + (labourCostPerSquareFoot != null ? labourCostPerSquareFoot.hashCode() : 0);
        result = 31 * result + (materialCost != null ? materialCost.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (labourCost != null ? labourCost.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }

}
