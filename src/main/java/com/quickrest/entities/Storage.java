package com.quickrest.entities;

public class Storage {
    private double totalExclusiveSum;
    private double totalInclusiveSum;
    private double vatSum;
    private static int index;

    public Storage() {
    }

    public double getTotalExclusiveSum() {
        return totalExclusiveSum;
    }

    public void setTotalExclusiveSum(double totalExclusiveSum) {
        this.totalExclusiveSum = totalExclusiveSum;
    }

    public double getTotalInclusiveSum() {
        return totalInclusiveSum;
    }

    public void setTotalInclusiveSum(double totalInclusiveSum) {
        this.totalInclusiveSum = totalInclusiveSum;
    }

    public double getVatSum() {
        return vatSum;
    }

    public void setVatSum(double vatSum) {
        this.vatSum = vatSum;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        Storage.index = index;
    }
}
