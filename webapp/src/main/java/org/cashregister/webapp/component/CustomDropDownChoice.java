package org.cashregister.webapp.component;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Created by derkhumblet on 02/04/16.
 */
public class CustomDropDownChoice<T> extends DropDownChoice<T> {
    private String displayValue;

    public CustomDropDownChoice(String id, String displayValue, IModel<T> model, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        this.setNullValid(displayValue != null);
        this.displayValue = displayValue;
    }

    @Override
    protected String getNullKeyDisplayValue() {
        return displayValue;
    }

    @Override
    protected String getNullValidDisplayValue() {
        return displayValue;
    }

    @Override
    protected String getNullValidKey() {
        return displayValue;
    }
}
