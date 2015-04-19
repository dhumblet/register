package org.cashregister.webapp.component;

import org.apache.wicket.datetime.DateConverter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by derkhumblet on 06/03/15.
 */
public class CustomDateConverter extends DateConverter {

    public CustomDateConverter() {
        super(true);
    }

    @Override
    protected DateTimeFormatter getFormat(Locale locale) {
        return DateTimeFormat.forPattern(getDatePattern(locale)).withLocale(locale);
    }

    @Override
    public String getDatePattern(Locale locale) {
        return "EEEE dd MMMM yyyy";
    }


}
