package com.quickrest.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

public class TransitData implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal id;
    private LocalDate dateGenerated;
    private BigInteger branchCnNumber;
    private String cnType;
    private String branchTo;
    private long grnNumber;
    private String details;
    private double cnCost;
    private String userName;
    private BigInteger hqCnNumber;
    private byte [] cnFile;
    private byte [] cnDetails;
    private String banchFrom;
    //private CreditNote creditNote;


    public TransitData() {
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public LocalDate getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(LocalDate dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public BigInteger getBranchCnNumber() {
        return branchCnNumber;
    }

    public void setBranchCnNumber(BigInteger branchCnNumber) {
        this.branchCnNumber = branchCnNumber;
    }

    public String getCnType() {
        return cnType;
    }

    public void setCnType(String cnType) {
        this.cnType = cnType;
    }

    public String getBranchTo() {
        return branchTo;
    }

    public void setBranchTo(String branchTo) {
        this.branchTo = branchTo;
    }

    public long getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(long grnNumber) {
        this.grnNumber = grnNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getCnCost() {
        return cnCost;
    }

    public void setCnCost(double cnCost) {
        this.cnCost = cnCost;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigInteger getHqCnNumber() {
        return hqCnNumber;
    }

    public void setHqCnNumber(BigInteger hqCnNumber) {
        this.hqCnNumber = hqCnNumber;
    }

    public byte[] getCnFile() {
        return cnFile;
    }

    public void setCnFile(byte[] cnFile) {
        this.cnFile = cnFile;
    }

    public byte[] getCnDetails() {
        return cnDetails;
    }

    public void setCnDetails(byte[] cnDetails) {
        this.cnDetails = cnDetails;
    }

    public String getBanchFrom() {
        return banchFrom;
    }

    public void setBanchFrom(String banchFrom) {
        this.banchFrom = banchFrom;
    }

//    public CreditNote getCreditNote() {
//        return creditNote;
//    }
//
//    public void setCreditNote(CreditNote creditNote) {
//        this.creditNote = creditNote;
//    }
}
