package com.quickrest.entities;

public class LPOPrice {
    private String code;
    private String Description;
    private double costIncl;
    private double vat;
    private double costExcl;
    private double total;
    private int quantity;
    private String units;
    private String barcode;

    public LPOPrice() {
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

    public double getCostExcl() {
        return costExcl;
    }

    public void setCostExcl(double costExcl) {
        this.costExcl = costExcl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getCostIncl() {
        return costIncl;
    }

    public void setCostIncl(double costIncl) {
        this.costIncl = costIncl;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
