package com.quickrest.entities;

import java.io.Serializable;

public class HQInvoiceNumber implements Serializable {
    private long hqInvoiceNumber;
    private Invoice invoice;

    public long getHqInvoiceNumber() {
        return hqInvoiceNumber;
    }

    public void setHqInvoiceNumber(long hqInvoiceNumber) {
        this.hqInvoiceNumber = hqInvoiceNumber;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HQInvoiceNumber other = (HQInvoiceNumber) obj;
        return this.hqInvoiceNumber == other.hqInvoiceNumber;
    }

}
