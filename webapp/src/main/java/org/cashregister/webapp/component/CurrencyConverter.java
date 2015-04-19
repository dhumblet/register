package org.cashregister.webapp.component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.util.convert.converter.AbstractNumberConverter;

/**
 * Created by derkhumblet on 29/11/14.
 */
public class CurrencyConverter extends AbstractNumberConverter<BigDecimal> {
    private static final long serialVersionUID = 1L;

    public CurrencyConverter() { }

    @Override
    public NumberFormat getNumberFormat(Locale locale) {
        return new DecimalFormat("€ 0.00");
    }

    @Override
    public Class getTargetType() {
        return BigDecimal.class;
    }

    public BigDecimal convertToObject(String value, Locale locale) {
        // string to double.
        try {
            return (BigDecimal) getNumberFormat(locale).parse(value);
        } catch (ParseException ex) {
            throw new WicketRuntimeException("CurrencyConverter");
        }
    }

    @Override
    public String convertToString(BigDecimal value, Locale locale) {
        return getNumberFormat(locale).format(value);
    }

}
