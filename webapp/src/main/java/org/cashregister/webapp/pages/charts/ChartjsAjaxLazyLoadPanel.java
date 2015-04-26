package org.cashregister.webapp.pages.charts;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;

import java.util.UUID;

public abstract class ChartjsAjaxLazyLoadPanel extends AjaxLazyLoadPanel {

	public ChartjsAjaxLazyLoadPanel(String id) {
		super(id);
	}

	@Override
	protected final void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		attributes.setChannel(new AjaxChannel(UUID.randomUUID().toString(), AjaxChannel.Type.QUEUE));
		super.updateAjaxAttributes(attributes);
	}

	@Override
	public final Component getLazyLoadComponent(String markupId) {
		return doGetLazyLoadComponent(markupId);
	}

	protected abstract Component doGetLazyLoadComponent(String markupId);
}
