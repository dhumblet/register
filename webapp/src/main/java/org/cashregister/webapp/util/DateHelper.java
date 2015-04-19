package org.cashregister.webapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by derkhumblet on 28/12/14.
 */
public final class DateHelper {

    public static Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static String getDateString(Date date) {
        DateFormat format = new SimpleDateFormat("EEEE dd MMM yyyy", new Locale("nl", "BE"));
        return format.format(date);
    }

    public static String getTimeString(Date date) {
        DateFormat format = new SimpleDateFormat("HH:mm", new Locale("nl", "BE"));
        return format.format(date);
    }
}

