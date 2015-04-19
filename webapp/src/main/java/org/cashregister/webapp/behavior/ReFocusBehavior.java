package org.cashregister.webapp.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Created by derkhumblet on 23/01/15.
 */
public class ReFocusBehavior extends AjaxEventBehavior {
    private Component component;

    public ReFocusBehavior(Component component) {
        super("onclick");
        this.component = component;
    }

    @Override
    protected void onEvent(AjaxRequestTarget target) {
        target.focusComponent(component);
    }
}
