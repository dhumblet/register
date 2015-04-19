package org.cashregister.webapp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by derkhumblet on 11/12/14.
 */
public final class BigDecimalHelper {

    public static BigDecimal asBigDecimal(String value) {
        DecimalFormat df = new DecimalFormat("########0.00");
        df.setParseBigDecimal(true);
        try {
            return (BigDecimal) df.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String asString(BigDecimal value) {
        DecimalFormat df = new DecimalFormat("########0.00");
        df.setParseBigDecimal(true);
        return "€" + df.format(value);
    }

    public static boolean isPositiveAmount(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0.009999999999999)) > 0;
    }

    public static boolean isNotZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) != 0;
    }
}

