package com.quickrest.entities;

import java.io.Serializable;
import java.util.Date;

public class ProductStock implements Serializable {
    private String code;
    private String description;
    private String supplier;
    private Date dateUpdated;
    private long docNumber;
    private int qtyOut;
    private int qtyIn;
    private double exclCost;
    private double inclCost;
    private double vat;
    private double markup;
    private int bal;
    private String transactionType;
    private long stockIndex;
    private int initialStockBal;

    public ProductStock() {
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

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public long getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(long docNumber) {
        this.docNumber = docNumber;
    }

    public int getQtyOut() {
        return qtyOut;
    }

    public void setQtyOut(int qtyOut) {
        this.qtyOut = qtyOut;
    }

    public int getQtyIn() {
        return qtyIn;
    }

    public void setQtyIn(int qtyIn) {
        this.qtyIn = qtyIn;
    }

    public double getExclCost() {
        return exclCost;
    }

    public void setExclCost(double exclCost) {
        this.exclCost = exclCost;
    }

    public double getInclCost() {
        return inclCost;
    }

    public void setInclCost(double inclCost) {
        this.inclCost = inclCost;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getMarkup() {
        return markup;
    }

    public void setMarkup(double markup) {
        this.markup = markup;
    }

    public int getBal() {
        return bal;
    }

    public void setBal(int bal) {
        this.bal = bal;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public long getStockIndex() {
        return stockIndex;
    }

    public void setStockIndex(long stockIndex) {
        this.stockIndex = stockIndex;
    }

    public int getInitialStockBal() {
        return initialStockBal;
    }

    public void setInitialStockBal(int initialStockBal) {
        this.initialStockBal = initialStockBal;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }


}
