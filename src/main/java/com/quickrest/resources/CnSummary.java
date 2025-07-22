package com.quickrest.resources;

import com.quickrest.entities.CompanyDetails;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class CnSummary implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private int quantity;
    private double cost;
    private String units;
    private double totalCost;
    private CompanyDetails companyDetails;

    public CnSummary() {
    }

    public CompanyDetails getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(CompanyDetails companyDetails) {
        this.companyDetails = companyDetails;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
