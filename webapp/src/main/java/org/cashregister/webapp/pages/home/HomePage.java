package org.cashregister.webapp.pages.home;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by derkhumblet on 10/11/14.
 */
public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
        super();
        add(new Label("message", "Hello World, Wicket"));
    }
}
