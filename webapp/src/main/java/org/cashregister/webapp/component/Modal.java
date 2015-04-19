package org.cashregister.webapp.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

/**
 * Created by derkhumblet on 23/12/14.
 */
public abstract class Modal extends WebMarkupContainer {
    private Form form;
    protected ModalCallback callback;

    public Modal(String id, Form form, ModalCallback callback) {
        super(id);
        this.form = form;
        this.callback = callback;
        add(form.setOutputMarkupId(true));
    }

    public Form getForm() {
        return this.form;
    }

    public abstract Component getFocusElement();

    public Component getFocusAlternativeElement() {
        return getFocusElement();
    }

    public static String showAndFocus(Component modal, Component field) {
        StringBuilder sb = new StringBuilder();
        sb.append("$('#").append(modal.getId()).append("')").append(".on('shown.bs.modal', function () {")
                .append("$('#").append(field.getId()).append("').focus();")
                .append("}).modal('show');");
        return sb.toString();
    }

    public static String hide(Component modal) {
        StringBuilder sb = new StringBuilder();
        sb.append("$('#").append(modal.getId()).append("').modal('hide');");
        return sb.toString();
    }
}
