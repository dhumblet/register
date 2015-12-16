package org.cashregister.webapp;

import org.apache.wicket.request.Request;
import org.cashregister.domain.Merchant;
import org.cashregister.domain.User;
import org.wicketstuff.security.WaspApplication;
import org.wicketstuff.security.WaspSession;

import java.util.Locale;

/**
 * Created by derkhumblet on 16/01/15.
 */
public class RegisterSession extends WaspSession {
    private static final long serialVersionUID = 3218303872691051392L;
    private User user;

    public RegisterSession(WaspApplication application, Request request) {
        super(application, request);
        super.setLocale(new Locale("nl", "BE"));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Merchant merchant() { return user.getMerchant(); }
}
