package org.cashregister.webapp.pages.configuration;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

public interface ConfigurationPageCallback extends Serializable {
    void edit(AjaxRequestTarget request);
}