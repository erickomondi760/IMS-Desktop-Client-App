package com.quickrest.entities;

import java.io.Serializable;
import java.util.Date;

public class ProductLiveStock implements Serializable {
    private String code;
    private String supplier;
    private String description;
    private Date lastReceived;
    private int liveStock;
    private String subDepartment;
    private String department;
    private int thirtyDaysMovement;
    private int sixtyDaysMovement;
    private double costInclusive;
    private double totalCostInclusive;
    private double costExclusive;
    private double totalCostExclusive;
    private int initialStockBal;


    public ProductLiveStock() {
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


    public int getLiveStock() {
        return liveStock;
    }

    public void setLiveStock(int liveStock) {
        this.liveStock = liveStock;
    }

    public int getThirtyDaysMovement() {
        return thirtyDaysMovement;
    }

    public void setThirtyDaysMovement(int thirtyDaysMovement) {
        this.thirtyDaysMovement = thirtyDaysMovement;
    }

    public int getSixtyDaysMovement() {
        return sixtyDaysMovement;
    }

    public void setSixtyDaysMovement(int sixtyDaysMovement) {
        this.sixtyDaysMovement = sixtyDaysMovement;
    }

    public Date getLastReceived() {
        return lastReceived;
    }

    public void setLastReceived(Date lastReceived) {
        this.lastReceived = lastReceived;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(String subDepartment) {
        this.subDepartment = subDepartment;
    }

    public double getCostInclusive() {
        return costInclusive;
    }

    public void setCostInclusive(double costInclusive) {
        this.costInclusive = costInclusive;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getTotalCostInclusive() {
        return totalCostInclusive;
    }

    public void setTotalCostInclusive(double totalCostInclusive) {
        this.totalCostInclusive = totalCostInclusive;
    }

    public double getCostExclusive() {
        return costExclusive;
    }

    public void setCostExclusive(double costExclusive) {
        this.costExclusive = costExclusive;
    }

    public double getTotalCostExclusive() {
        return totalCostExclusive;
    }

    public void setTotalCostExclusive(double totalCostExclusive) {
        this.totalCostExclusive = totalCostExclusive;
    }

    public int getInitialStockBal() {
        return initialStockBal;
    }

    public void setInitialStockBal(int initialStockBal) {
        this.initialStockBal = initialStockBal;
    }
}
