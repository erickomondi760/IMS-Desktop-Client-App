package com.quickrest.resources;

import com.quickrest.entities.BranchDetails;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class ReceivedGrn implements Serializable {
    private static final long serialVersionUID = 1L;

    private SimpleStringProperty code;
    private SimpleStringProperty barcode;
    private SimpleStringProperty description;
    private SimpleStringProperty invoiceNumber;
    private Date grnDate;
    private Date taxDate;
    private SimpleStringProperty supplier;
    private SimpleIntegerProperty qtyOrdered;
    private SimpleIntegerProperty qtyReceived;
    private SimpleIntegerProperty balQty;
    private SimpleDoubleProperty costExclusive;
    private SimpleDoubleProperty costInclusive;
    private SimpleDoubleProperty discount;
    private SimpleDoubleProperty amountReceivedExcl;
    private SimpleDoubleProperty amountReceivedIncl;
    private SimpleDoubleProperty amountReceivedVat;
    private SimpleStringProperty cuInvoiceNumber;
    private SimpleStringProperty norminalAcc;
    private String units;
    private String packaging;
    private double vat;
    private long branchGrnNumber;
    private long hqGrnNumber;
    private BranchDetails branchDetails;
    private String userName;
    private LocalDate invoiceDate;
    private String branchName;

    public ReceivedGrn() {
        this.code = new SimpleStringProperty();
        this.barcode = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.invoiceNumber = new SimpleStringProperty();
        this.supplier = new SimpleStringProperty();
        this.qtyOrdered = new SimpleIntegerProperty();
        this.qtyReceived = new SimpleIntegerProperty();
        this.balQty = new SimpleIntegerProperty();
        this.costExclusive = new SimpleDoubleProperty();
        this.costInclusive = new SimpleDoubleProperty();
        this.discount = new SimpleDoubleProperty();
        this.amountReceivedExcl = new SimpleDoubleProperty();
        this.amountReceivedIncl = new SimpleDoubleProperty();
        this.amountReceivedVat = new SimpleDoubleProperty();
        this.cuInvoiceNumber = new SimpleStringProperty();
        this.norminalAcc = new SimpleStringProperty();
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BranchDetails getBranchDetails() {
        return branchDetails;
    }

    public void setBranchDetails(BranchDetails branchDetails) {
        this.branchDetails = branchDetails;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCode() {
        return code.get();
    }
    public void setCode(String code) {
        this.code.set(code);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public Date getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(Date grnDate) {
        this.grnDate = grnDate;
    }

    public Date getTaxDate() {
        return taxDate;
    }

    public void setTaxDate(Date taxDate) {
        this.taxDate = taxDate;
    }

    public String getSupplier() {
        return supplier.get();
    }

    public void setSupplier(String supplier) {
        this.supplier.set(supplier);
    }

    public int getQtyOrdered() {
        return qtyOrdered.get();
    }

    public void setQtyOrdered(int qtyOrdered) {
        this.qtyOrdered.set(qtyOrdered);
    }

    public int getQtyReceived() {
        return qtyReceived.get();
    }

    public void setQtyReceived(int qtyReceived) {
        this.qtyReceived.set(qtyReceived);
    }

    public int getBalQty() {
        return balQty.get();
    }

    public void setBalQty(int balQty) {
        this.balQty.set(balQty);
    }

    public double getCostExclusive() {
        return costExclusive.get();
    }

    public void setCostExclusive(double costExclusive) {
        this.costExclusive.set(costExclusive);
    }

    public double getCostInclusive() {
        return costInclusive.get();
    }

    public void setCostInclusive(double costInclusive) {
        this.costInclusive.set(costInclusive);
    }

    public double getDiscount() {
        return discount.get();
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }

    public double getAmountReceivedExcl() {
        return amountReceivedExcl.get();
    }

    public void setAmountReceivedExcl(double amountReceived) {
        this.amountReceivedExcl.set(amountReceived);
    }

    public double getAmountReceivedIncl() {
        return amountReceivedIncl.get();
    }


    public void setAmountReceivedIncl(double amountReceivedIncl) {
        this.amountReceivedIncl.set(amountReceivedIncl);
    }

    public double getAmountReceivedVat() {
        return amountReceivedVat.get();
    }

    public void setAmountReceivedVat(double amountReceivedVat) {
        this.amountReceivedVat.set(amountReceivedVat);
    }

    public String getCuInvoiceNumber() {
        return cuInvoiceNumber.get();
    }

    public SimpleStringProperty cuInvoiceNumberProperty() {
        return cuInvoiceNumber;
    }

    public void setCuInvoiceNumber(String cuInvoiceNumber) {
        this.cuInvoiceNumber.set(cuInvoiceNumber);
    }

    public String getNorminalAcc() {
        return norminalAcc.get();
    }

    public SimpleStringProperty norminalAccProperty() {
        return norminalAcc;
    }

    public void setNorminalAcc(String norminalAcc) {
        this.norminalAcc.set(norminalAcc);
    }

    public long getBranchGrnNumber() {
        return branchGrnNumber;
    }

    public void setBranchGrnNumber(long branchGrnNumber) {
        this.branchGrnNumber = branchGrnNumber;
    }

    public long getHqGrnNumber() {
        return hqGrnNumber;
    }

    public void setHqGrnNumber(long hqGrnNumber) {
        this.hqGrnNumber = hqGrnNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
