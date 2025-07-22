package com.quickrest.resources;

import java.io.Serializable;
import java.math.BigInteger;

public class PODetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String supplier;
    private String code;
    private String description;
    private int quantity;
    private double costExclusive;
    private double totalCost;
    private String barcode;
    private double lpoAmount;
    private BigInteger id;
    private String units;
    private String packaging;
    private double costInclusive;
    private double vat;
    private double markUp;
    private double sp;
    private long lpoNumber;
    private int terms;



    public PODetails() {
    }

    public long getLpoNumber() {
        return lpoNumber;
    }

    public void setLpoNumber(long lpoNumber) {
        this.lpoNumber = lpoNumber;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public double getMarkUp() {
        return markUp;
    }

    public void setMarkUp(double markUp) {
        this.markUp = markUp;
    }

    public double getSp() {
        return sp;
    }

    public void setSp(double sp) {
        this.sp = sp;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getCostInclusive() {
        return costInclusive;
    }

    public void setCostInclusive(double costInclusive) {
        this.costInclusive = costInclusive;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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

    public double getCostExclusive() {
        return costExclusive;
    }

    public void setCostExclusive(double costExclusive) {
        this.costExclusive = costExclusive;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getLpoAmount() {
        return lpoAmount;
    }

    public void setLpoAmount(double lpoAmount) {
        this.lpoAmount = lpoAmount;
    }

    public int getTerms() {
        return terms;
    }

    public void setTerms(int terms) {
        this.terms = terms;
    }
}
