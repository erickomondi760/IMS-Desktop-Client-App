package com.quickrest.entities;

public class HqGrnNumber {
    private long grnNumber;
    private GoodsReceivedNote grn;

    public HqGrnNumber() {
    }

    public long getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(long grnNumber) {
        this.grnNumber = grnNumber;
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }
}
