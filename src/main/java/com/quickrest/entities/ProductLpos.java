package com.quickrest.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;

public class ProductLpos implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private long lpoNumber;
    private LocalDate dateRaised;
    private String supplier;
    private double amount;
    private String received;
    private String status;
    private String userName;
    boolean selected;
    private byte [] lpoFile;
    private String branchName;
    private LocalDate expiryDate;
    private byte[] poDetails;
    private PoNumber poNumber;


    public ProductLpos() {
    }

    public PoNumber getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(PoNumber poNumber) {
        this.poNumber = poNumber;
    }

    public byte[] getPoDetails() {
        return poDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPoDetails(byte[] poDetails) {
        this.poDetails = poDetails;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public byte[] getLpoFile() {
        return lpoFile;
    }

    public void setLpoFile(byte[] lpoFile) {
        this.lpoFile = lpoFile;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public long getLpoNumber() {
        return lpoNumber;
    }

    public void setLpoNumber(long lpoNumber) {
        this.lpoNumber = lpoNumber;
    }

    public LocalDate getDateRaised() {
        return dateRaised;
    }

    public void setDateRaised(LocalDate dateRaised) {
        this.dateRaised = dateRaised;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
