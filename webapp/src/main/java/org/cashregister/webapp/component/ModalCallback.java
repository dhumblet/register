package org.cashregister.webapp.component;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

/**
 * Created by derkhumblet on 23/12/14.
 */
public interface ModalCallback extends Serializable {

    void ok(AjaxRequestTarget target);
    void close(AjaxRequestTarget target);
}
