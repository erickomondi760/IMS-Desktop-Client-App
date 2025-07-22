package com.quickrest.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class HqInvoice implements Serializable {
    private LocalDate invoiceDate;
    private long invoiceNumber;
    private String status;
    private String userName;
    private byte[] invoiceFile;
    private long hqInvoiceNumber;
    private double invoiceSubTotal;
    private double invoiceTotal;
    private double invoiceVat;
    private double invoiceDiscount;
    private String branchName;
    private Invoice invoice;


    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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


    public long getHqInvoiceNumber() {
        return hqInvoiceNumber;
    }

    public void setHqInvoiceNumber(long hqInvoiceNumber) {
        this.hqInvoiceNumber = hqInvoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public byte[] getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(byte[] invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public double getInvoiceDiscount() {
        return invoiceDiscount;
    }

    public void setInvoiceDiscount(double invoiceDiscount) {
        this.invoiceDiscount = invoiceDiscount;
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

}
