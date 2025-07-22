package com.quickrest.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class Invoice extends PaymentDetails implements Serializable {
    private LocalDate invoiceDate;
    private long invoiceNumber;
    private String transporter;
    private String status;
    private String userName;
    private byte[] invoiceFile;
    private long hqInvoiceNumber;
    private BranchDetails branchDetails;
    private double invoiceSubTotal;
    private double invoiceTotal;
    private double invoiceVat;
    private double invoiceDiscount;
    private String clientName;
    private String outletName;
    private String branchName;
    private String companyName;
    byte[] createdInvoice;
    private HqInvoice hqInvoice;
    private HQInvoiceNumber hqInvNumber;
    private InvoiceNumber invoiceNum;

    public Invoice() {
    }

    public InvoiceNumber getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(InvoiceNumber invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public HQInvoiceNumber getHqInvNumber() {
        return hqInvNumber;
    }

    public void setHqInvNumber(HQInvoiceNumber hqInvNumber) {
        this.hqInvNumber = hqInvNumber;
    }

    public HqInvoice getHqInvoice() {
        return hqInvoice;
    }

    public void setHqInvoice(HqInvoice hqInvoice) {
        this.hqInvoice = hqInvoice;
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

    public long getHqInvoiceNumber() {
        return hqInvoiceNumber;
    }

    public void setHqInvoiceNumber(long hqInvoiceNumber) {
        this.hqInvoiceNumber = hqInvoiceNumber;
    }

    public BranchDetails getBranchDetails() {
        return branchDetails;
    }

    public void setBranchDetails(BranchDetails branchDetails) {
        this.branchDetails = branchDetails;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public byte[] getCreatedInvoice() {
        return createdInvoice;
    }

    public void setCreatedInvoice(byte[] createdInvoice) {
        this.createdInvoice = createdInvoice;
    }
}
