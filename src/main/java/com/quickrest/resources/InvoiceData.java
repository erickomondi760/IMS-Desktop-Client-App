package com.quickrest.resources;

import com.quickrest.entities.*;

import java.io.Serializable;
import java.time.LocalDate;

public class InvoiceData implements Serializable {
    private static final long serialVersionUID = 7L;
    private double costExclusive;
    private String code;
    private double vat;
    private double costInclusive;
    private double disc;
    private int quantity;
    private LocalDate invoiceDate;
    private long invoiceNumber;
    private String transporter;
    private double subTotal;
    private String branchName;
    private double totalVat;
    private double total;
    private String status;
    private String userName;
    private byte[] invoiceFile;
    private BranchDetails branchDetails;
    private long hqInvoiceNumber;
    private String description;
    private String barcode;
    private String vehicle;
    private double amountExclusive;
    private double amountInclusive;
    private double vatAmount;
    private String packaging;
    private String units;
    private double invoiceSubTotal;
    private double invoiceTotal;
    private double invoiceVat;
    private double invoiceDiscount;
    private String clientName;
    private String clientAddress;
    private String clientEmail;
    private String outletLocation;
    private String outletEmail;
    private String outletPnoneNumber;
    private CompanyDetails companyDetails;

    public double getCostExclusive() {
        return costExclusive;
    }

    public void setCostExclusive(double costExclusive) {
        this.costExclusive = costExclusive;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public double getDisc() {
        return disc;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public double getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(double totalVat) {
        this.totalVat = totalVat;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(byte[] invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public BranchDetails getBranchDetails() {
        return branchDetails;
    }

    public void setBranchDetails(BranchDetails branchDetails) {
        this.branchDetails = branchDetails;
    }

    public long getHqInvoiceNumber() {
        return hqInvoiceNumber;
    }

    public void setHqInvoiceNumber(long hqInvoiceNumber) {
        this.hqInvoiceNumber = hqInvoiceNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public double getAmountExclusive() {
        return amountExclusive;
    }

    public void setAmountExclusive(double amountExclusive) {
        this.amountExclusive = amountExclusive;
    }

    public double getAmountInclusive() {
        return amountInclusive;
    }

    public void setAmountInclusive(double amountInclusive) {
        this.amountInclusive = amountInclusive;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getInvoiceSubTotal() {
        return invoiceSubTotal;
    }

    public void setInvoiceSubTotal(double invoiceSubTotal) {
        this.invoiceSubTotal = invoiceSubTotal;
    }

    public double getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(double invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public double getInvoiceVat() {
        return invoiceVat;
    }

    public void setInvoiceVat(double invoiceVat) {
        this.invoiceVat = invoiceVat;
    }

    public double getInvoiceDiscount() {
        return invoiceDiscount;
    }

    public void setInvoiceDiscount(double invoiceDiscount) {
        this.invoiceDiscount = invoiceDiscount;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getOutletLocation() {
        return outletLocation;
    }

    public void setOutletLocation(String outletLocation) {
        this.outletLocation = outletLocation;
    }

    public String getOutletEmail() {
        return outletEmail;
    }

    public void setOutletEmail(String outletEmail) {
        this.outletEmail = outletEmail;
    }

    public String getOutletPnoneNumber() {
        return outletPnoneNumber;
    }

    public void setOutletPnoneNumber(String outletPnoneNumber) {
        this.outletPnoneNumber = outletPnoneNumber;
    }

    public CompanyDetails getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(CompanyDetails companyDetails) {
        this.companyDetails = companyDetails;
    }
}
