package org.cashregister.webapp.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

/**
 * Created by derkhumblet on 24/11/14.
 */
public class FocusOnLoadBehavior extends Behavior {
    private Component component;

    public FocusOnLoadBehavior() {
        super();
    }

    public FocusOnLoadBehavior(Component component) {
        super();
        this.component = component;
    }

    public void bind(Component component) {
        this.component = component;
        component.setOutputMarkupId(true);
    }

    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.render(OnDomReadyHeaderItem.forScript("document.getElementById('" + component.getMarkupId() + "').focus()"));
    }

    public boolean isTemporary() {
        return true;
    }
}
