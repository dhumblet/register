package org.cashregister.webapp.component;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Created by derkhumblet on 11/12/14.
 */
public class CustomFeedbackPanel extends FeedbackPanel {

    public CustomFeedbackPanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
    }

    public CustomFeedbackPanel(String id) {
        super(id);
    }

    @Override
    protected String getCSSClass(FeedbackMessage message) {
        switch (message.getLevel()) {
            case FeedbackMessage.DEBUG:
                return "alert-info";
            case FeedbackMessage.ERROR:
                return "alert-danger";
            case FeedbackMessage.FATAL:
                return "alert-danger";
            case FeedbackMessage.INFO:
                return "alert-info";
            case FeedbackMessage.SUCCESS:
                return "alert-success";
            case FeedbackMessage.UNDEFINED:
                return "alert-info";
            case FeedbackMessage.WARNING:
                return "alert-warning";
            default:
                return "alert-info";
        }
    }
}
