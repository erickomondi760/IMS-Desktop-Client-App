package com.quickrest.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class GoodsReceivedNote implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
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
    private byte []grnDetails;
    private String branchName;
    private HqGrn hqGrn;
    private HqGrnNumber hqGrnNum;
    private BranchGrnNumber branchGrnNum;

    public GoodsReceivedNote() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HqGrn getHqGrn() {
        return hqGrn;
    }

    public void setHqGrn(HqGrn hqGrn) {
        this.hqGrn = hqGrn;
    }

    public HqGrnNumber getHqGrnNum() {
        return hqGrnNum;
    }

    public void setHqGrnNum(HqGrnNumber hqGrnNum) {
        this.hqGrnNum = hqGrnNum;
    }

    public BranchGrnNumber getBranchGrnNum() {
        return branchGrnNum;
    }

    public void setBranchGrnNum(BranchGrnNumber branchGrnNum) {
        this.branchGrnNum = branchGrnNum;
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

    public byte[] getGrnDetails() {
        return grnDetails;
    }

    public void setGrnDetails(byte[] grnDetails) {
        this.grnDetails = grnDetails;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
