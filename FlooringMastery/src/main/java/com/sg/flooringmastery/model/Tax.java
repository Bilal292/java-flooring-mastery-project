package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Tax {
    private String state;
    private String stateAbr;
    private BigDecimal taxRate;

    public Tax(String state, String stateAbr, BigDecimal taxRate) {
        this.state = state;
        this.stateAbr = stateAbr;
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    public String getState() {
        return state;
    }

    public String getStateAbr() {
        return stateAbr;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    @Override
    public String toString() {
        return "Tax{" +
                "state='" + state + '\'' +
                ", stateAbr='" + stateAbr + '\'' +
                ", taxRate=" + taxRate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tax tax = (Tax) o;

        if (!Objects.equals(state, tax.state)) return false;
        if (!Objects.equals(stateAbr, tax.stateAbr)) return false;
        return Objects.equals(taxRate, tax.taxRate);
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (stateAbr != null ? stateAbr.hashCode() : 0);
        result = 31 * result + (taxRate != null ? taxRate.hashCode() : 0);
        return result;
    }
}
