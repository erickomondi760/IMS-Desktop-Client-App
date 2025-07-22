package com.quickrest.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class CreditNote implements Serializable {
    private static final long serialVersionUID = 1L;


    private BigDecimal id;
    private Date dateGenerated;
    private BigInteger branchCnNumber;
    private String cnType;
    private String entityName;
    private String details;
    private double cnCost;
    private String userName;
    private BigInteger hqCnNumber;
    private byte [] cnFile;
    private byte [] cnDetails;
    private BranchCnNumber branchCnNum;
    private String branchName;


    public CreditNote() {
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
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

    public BranchCnNumber getBranchCnNum() {
        return branchCnNum;
    }

    public void setBranchCnNum(BranchCnNumber branchCnNum) {
        this.branchCnNum = branchCnNum;
    }

    public Date getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(Date dateGenerated) {
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
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
}
