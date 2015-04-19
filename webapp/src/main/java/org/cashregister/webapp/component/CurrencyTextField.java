package org.cashregister.webapp.component;

import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Created by derkhumblet on 12/12/14.
 */
public class CurrencyTextField extends NumberTextField {
    private static final long serialVersionUID = 1L;

    public CurrencyTextField(String id) {
        this(id, null);
    }

    public CurrencyTextField(String id, IModel model) {
        super(id, model);
    }

    @Override
    public IConverter getConverter(Class type) {
        return new CurrencyConverter();
    }

}
