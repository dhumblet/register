package org.cashregister.webapp.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;

/**
 * Created by derkhumblet on 12/12/14.
 */
public class PlaceholderBehavior extends Behavior {
    private String resourceKey;
    private String text;

    /**
     * Creates a new {@link PlaceholderBehavior} with the given resource key
     *
     * @param resourceKey the key for the translation
     * @return a new behavior
     */
    public static PlaceholderBehavior withKey(String resourceKey) {
        PlaceholderBehavior b = new PlaceholderBehavior();
        b.resourceKey = resourceKey;
        return b;
    }

    public static PlaceholderBehavior withText(String text) {
        PlaceholderBehavior b = new PlaceholderBehavior();
        b.text = text;
        return b;
    }

    @Override
    public void onConfigure(Component component) {
        super.onConfigure(component);
        if (!(component instanceof TextField)) {
            throw new IllegalStateException(getClass().getSimpleName() +  " can only be added to a " + TextField.class);
        }
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        super.onComponentTag(component, tag);
        if (resourceKey == null) {
            tag.put("placeholder", text);
        } else {
            tag.put("placeholder", component.getString(resourceKey));
        }
    }
}
