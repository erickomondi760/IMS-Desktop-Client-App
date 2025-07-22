package com.quickrest.entities;

import java.math.BigInteger;

public class PoNumber {
    private BigInteger id;
    private long lpoNumber;
    ProductLpos lpo;

    public PoNumber() {

    }

    public ProductLpos getLpo() {
        return lpo;
    }

    public void setLpo(ProductLpos lpo) {
        this.lpo = lpo;
    }

    public long getLpoNumber() {
        return lpoNumber;
    }

    public void setLpoNumber(long lpoNumber) {
        this.lpoNumber = lpoNumber;
    }
}
