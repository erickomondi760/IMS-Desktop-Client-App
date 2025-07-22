package com.quickrest.entities;

import java.io.Serializable;
import java.util.Date;

public class ProductStockValue implements Serializable {
    private Date dateRacalculated;
    private double exclusiveValue;
    private double inclusiveValue;

    public Date getDateRacalculated() {
        return dateRacalculated;
    }

    public void setDateRacalculated(Date dateRacalculated) {
        this.dateRacalculated = dateRacalculated;
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
