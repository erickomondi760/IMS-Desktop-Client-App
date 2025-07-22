package com.quickrest.entities;

public class BranchGrnNumber {
    private long grnNumber;
    private GoodsReceivedNote grn;

    public BranchGrnNumber() {
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }

    public long getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(long grnNumber) {
        this.grnNumber = grnNumber;
    }
}
