package org.cashregister.webapp.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by derkhumblet on 24/11/14.
 */
public class ShortcutBehavior extends Behavior {
    private Component component;
    Map<String, String> shortcuts = new HashMap<>();

    public void bind(Component component) {
        this.component = component;
        component.setOutputMarkupId(true);
    }

    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        StringBuilder script = new StringBuilder();
        for (Map.Entry<String, String> shortcut : shortcuts.entrySet()) {
            script.append(getShortcutScript(shortcut.getKey(), shortcut.getValue()));
        }
        response.render(OnDomReadyHeaderItem.forScript(script.toString()));
    }

    public void addShortcut(String key, String action) {
        shortcuts.put(key, action);
    }

    private String getShortcutScript(final String key, final String action) {
        StringBuilder script = new StringBuilder();
        script
            .append("shortcut.add(\"").append(key).append("\", function() {\n")
            .append("\t").append(action).append(";\n")
            .append("} ,{\n")
            .append("\t'type':'keydown',\n")
            .append("\t'propagate':true,\n")
            .append("\t'target':document\n")
            .append("});\n");
        return script.toString();
    }

    public boolean isTemporary() {
        return true;
    }
}
