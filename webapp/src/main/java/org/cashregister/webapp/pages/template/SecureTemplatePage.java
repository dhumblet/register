package org.cashregister.webapp.pages.template;

import org.cashregister.domain.Merchant;
import org.cashregister.webapp.RegisterSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.security.actions.WaspAction;
import org.wicketstuff.security.checks.ComponentSecurityCheck;
import org.wicketstuff.security.checks.ISecurityCheck;
import org.wicketstuff.security.components.ISecurePage;
import org.wicketstuff.security.components.SecureComponentHelper;

/**
 * Created by derkhumblet on 16/01/15.
 */
public abstract class SecureTemplatePage extends TemplatePage implements ISecurePage {
    private static final Logger LOG = LoggerFactory.getLogger(SecureTemplatePage.class);

    private static final long serialVersionUID = -3282908068322044791L;

    public SecureTemplatePage() {
        super();
        setSecurityCheck(new ComponentSecurityCheck(this));
    }

    @Override
    public final void setSecurityCheck(ISecurityCheck check) {
        SecureComponentHelper.setSecurityCheck(this, check);
    }

    @Override
    public final ISecurityCheck getSecurityCheck() {
        return SecureComponentHelper.getSecurityCheck(this);
    }

    @Override
    public boolean isActionAuthorized(String action) {
        return SecureComponentHelper.isActionAuthorized(this, action);
    }

    @Override
    public boolean isActionAuthorized(WaspAction action) {
        return SecureComponentHelper.isActionAuthorized(this, action);
    }

    @Override
    public boolean isAuthenticated() {
        return SecureComponentHelper.isAuthenticated(this);
    }

    protected RegisterSession getRegisterSession() {
        return (RegisterSession) getSession();
    }

    protected long getMerchantId() {
        if (getMerchant() != null) {
            return getRegisterSession().getUser().getMerchant().getId();
        }
        return -1;
    }

    protected Merchant getMerchant() {
        if (getRegisterSession().getUser() != null) {
            return getRegisterSession().getUser().getMerchant();
        }
        return null;
    }
}
