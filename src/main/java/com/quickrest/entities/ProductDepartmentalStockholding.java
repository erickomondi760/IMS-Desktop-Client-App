package com.quickrest.entities;

import java.io.Serializable;

public class ProductDepartmentalStockholding implements Serializable {
    private String subDepartment;
    private String department;
    private double exclusiveValue;
    private double inclusiveValue;

    public String getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(String subDepartment) {
        this.subDepartment = subDepartment;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getExclusiveValue() {
        return exclusiveValue;
    }

    public void setExclusiveValue(double exclusiveValue) {
        this.exclusiveValue = exclusiveValue;
    }

    public double getInclusiveValue() {
        return inclusiveValue;
    }

    public void setInclusiveValue(double inclusiveValue) {
        this.inclusiveValue = inclusiveValue;
    }
}
