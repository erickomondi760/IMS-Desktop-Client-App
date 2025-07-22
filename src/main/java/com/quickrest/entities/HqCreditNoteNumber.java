package com.quickrest.entities;

public class HqCreditNoteNumber {
    private long cnNumber;
    private CreditNote creditNote;

    public long getCnNumber() {
        return cnNumber;
    }

    public void setCnNumber(long cnNumber) {
        this.cnNumber = cnNumber;
    }

    public CreditNote getCreditNote() {
        return creditNote;
    }

    public void setCreditNote(CreditNote creditNote) {
        this.creditNote = creditNote;
    }
}
