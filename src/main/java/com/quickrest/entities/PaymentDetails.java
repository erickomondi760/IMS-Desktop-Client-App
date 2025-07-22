package com.quickrest.entities;

import java.io.Serializable;

public class PaymentDetails implements Serializable {
    private static final String ACC_1 = "101010101001";
    private static final String ACC_2 = "101010101002";
    private static final String ACC_3 = "101010101003";
    private static final String PAYBILL_NUMBER = "101010101001";

    public static String getACC_1() {
        return ACC_1;
    }

    public static String getACC_2() {
        return ACC_2;
    }

    public static String getACC_3() {
        return ACC_3;
    }

    public static String getPAYBILL_NUMBER() {
        return PAYBILL_NUMBER;
    }
}
