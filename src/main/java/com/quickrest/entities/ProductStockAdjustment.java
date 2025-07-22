package com.quickrest.entities;

import java.io.Serializable;
import java.util.Date;

public class ProductStockAdjustment implements Serializable {
    private Date adjustmentDate;
    private int adjustmentNumber;
    private int adjustedQuantity;
    private String adjustmentReason;
    private String userName;
    private double adjustmentValue;
    private String department;
    private String code;
    private String description;

    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public int getAdjustmentNumber() {
        return adjustmentNumber;
    }

    public void setAdjustmentNumber(int adjustmentNumber) {
        this.adjustmentNumber = adjustmentNumber;
    }

    public int getAdjustedQuantity() {
        return adjustedQuantity;
    }

    public void setAdjustedQuantity(int adjustedQuantity) {
        this.adjustedQuantity = adjustedQuantity;
    }

    public String getAdjustmentReason() {
        return adjustmentReason;
    }

    public void setAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getAdjustmentValue() {
        return adjustmentValue;
    }

    public void setAdjustmentValue(double adjustmentValue) {
        this.adjustmentValue = adjustmentValue;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
}
