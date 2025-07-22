package com.quickrest.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class HqGrn implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal id;
    private String invoiceNumber;
    private String supplier;
    private double amountReceivedExcl;
    private double amountReceivedIncl;
    private double amountReceivedVat;
    private String cuInvoiceNumber;
    private String norminalAcc;
    private long hqGrnNumber;
    private long branchGrnNumber;
    private byte [] grnFile;
    private BranchDetails branchDetails;
    private String userName;
    private LocalDate invoiceDate;
    private LocalDate grnDate;
    private String branchName;
    private GoodsReceivedNote grn;
    private BranchDetails branch;

    public HqGrn() {
    }

    public BranchDetails getBranch() {
        return branch;
    }

    public void setBranch(BranchDetails branch) {
        this.branch = branch;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getAmountReceivedExcl() {
        return amountReceivedExcl;
    }

    public void setAmountReceivedExcl(double amountReceivedExcl) {
        this.amountReceivedExcl = amountReceivedExcl;
    }

    public double getAmountReceivedIncl() {
        return amountReceivedIncl;
    }

    public void setAmountReceivedIncl(double amountReceivedIncl) {
        this.amountReceivedIncl = amountReceivedIncl;
    }

    public double getAmountReceivedVat() {
        return amountReceivedVat;
    }

    public void setAmountReceivedVat(double amountReceivedVat) {
        this.amountReceivedVat = amountReceivedVat;
    }

    public String getCuInvoiceNumber() {
        return cuInvoiceNumber;
    }

    public void setCuInvoiceNumber(String cuInvoiceNumber) {
        this.cuInvoiceNumber = cuInvoiceNumber;
    }

    public String getNorminalAcc() {
        return norminalAcc;
    }

    public void setNorminalAcc(String norminalAcc) {
        this.norminalAcc = norminalAcc;
    }

    public long getHqGrnNumber() {
        return hqGrnNumber;
    }

    public void setHqGrnNumber(long hqGrnNumber) {
        this.hqGrnNumber = hqGrnNumber;
    }

    public long getBranchGrnNumber() {
        return branchGrnNumber;
    }

    public void setBranchGrnNumber(long branchGrnNumber) {
        this.branchGrnNumber = branchGrnNumber;
    }

    public byte[] getGrnFile() {
        return grnFile;
    }

    public void setGrnFile(byte[] grnFile) {
        this.grnFile = grnFile;
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

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(LocalDate grnDate) {
        this.grnDate = grnDate;
    }
}
