package org.cashregister.webapp.pages.product;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

/**
 * Created by derkhumblet on 21/12/14.
 */
public interface ProductPageCallback extends Serializable {
    void edit(AjaxRequestTarget request);
}
