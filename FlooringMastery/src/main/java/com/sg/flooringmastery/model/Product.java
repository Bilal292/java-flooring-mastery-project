package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Product {
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;

    public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot) {
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        this.laborCostPerSquareFoot = laborCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
    }

    public String getProductType() {
        return productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productType='" + productType + '\'' +
                ", costPerSquareFoot=" + costPerSquareFoot +
                ", laborCostPerSquareFoot=" + laborCostPerSquareFoot +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!Objects.equals(productType, product.productType)) return false;
        if (!Objects.equals(costPerSquareFoot, product.costPerSquareFoot))
            return false;
        return Objects.equals(laborCostPerSquareFoot, product.laborCostPerSquareFoot);
    }

    @Override
    public int hashCode() {
        int result = productType != null ? productType.hashCode() : 0;
        result = 31 * result + (costPerSquareFoot != null ? costPerSquareFoot.hashCode() : 0);
        result = 31 * result + (laborCostPerSquareFoot != null ? laborCostPerSquareFoot.hashCode() : 0);
        return result;
    }
}
