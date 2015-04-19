package org.cashregister.webapp.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Created by derkhumblet on 29/11/14.
 */
public class CurrencyLabel extends Label {
    private static final long serialVersionUID = 1L;

    public CurrencyLabel(String id) {
        this(id, null);
    }

    public CurrencyLabel(String id, IModel model) {
        super(id, model);
    }

    @Override
    public IConverter getConverter(Class type) {
        return new CurrencyConverter();
    }

}
