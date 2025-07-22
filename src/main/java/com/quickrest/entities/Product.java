package com.quickrest.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class Product implements Serializable {

    private final String errorText = "Error in network connection!";
    private BigInteger id;
    private String department;
    private String name;
    private String code;
    private String barcode;
    private String subDepartment;
    private String packaging;
    private String orderStatus;
    private String costStatus;
    private Date expiryDate;
    private Date productionDate;
    private int batchNumber;
    private Date listDate;
    private String createdBy;
    private byte[] image;
    private Supplier supplier;
    private String supplierName;
    private String unit;
    private Prices price;

    public Product() {
    }

    public Prices getPrice() {
        return price;
    }

    public void setPrice(Prices price) {
        this.price = price;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(String subDepartment) {
        this.subDepartment = subDepartment;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Date getListDate() {
        return listDate;
    }

    public void setListDate(Date listDate) {
        this.listDate = listDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public int hashCode() {
        int hash = 90;
        return hash = 4568*hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Product)
            return true;

        Product product = (Product) obj;
        if((this.id == null && product.id !=null) || (this.id != null && !product.id.equals(this.id)))
            return false;

        return true;
    }
}
