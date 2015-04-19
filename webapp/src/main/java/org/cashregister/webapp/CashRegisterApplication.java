package org.cashregister.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.cashregister.webapp.pages.error.ErrorPage;
import org.cashregister.webapp.pages.kassa.KassaPage;
import org.cashregister.webapp.pages.login.LoginPage;
import org.cashregister.webapp.pages.product.ProductPage;
import org.cashregister.webapp.pages.overview.OverviewPage;
import org.cashregister.webapp.pages.transaction.TransactionPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.security.hive.HiveMind;
import org.wicketstuff.security.hive.config.PolicyFileHiveFactory;
import org.wicketstuff.security.hive.config.SwarmPolicyFileHiveFactory;
import org.wicketstuff.security.swarm.SwarmWebApplication;

import java.util.TimeZone;

/**
 * Created by derkhumblet on 10/11/14.
 */
public class CashRegisterApplication extends SwarmWebApplication {
    private static final Logger LOG = LoggerFactory.getLogger(CashRegisterApplication.class);
    private static final String HIVE_KEY = "org.cashregister.webapp";

    @Override
    protected void init() {
        super.init();

        setTimeZone();

        getMarkupSettings().setDefaultBeforeDisabledLink(null);
        getMarkupSettings().setStripWicketTags(true);
        
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getDebugSettings().setAjaxDebugModeEnabled(false);
        getApplicationSettings().setPageExpiredErrorPage(KassaPage.class);
        getApplicationSettings().setAccessDeniedPage(ErrorPage.class);
        getApplicationSettings().setInternalErrorPage(ErrorPage.class);

        mountPage("/kassa", KassaPage.class);
        mountPage("/products", ProductPage.class);
        mountPage("/transactions", TransactionPage.class);
        mountPage("/overview", OverviewPage.class);
        mountPage("/login", LoginPage.class);
        mountPage("/error", ErrorPage.class);
        mountPage("/error/${http}", ErrorPage.class);


        getRequestCycleListeners().add(new AbstractRequestCycleListener() {
            @Override
            public IRequestHandler onException(RequestCycle cycle, Exception e) {
                LOG.error(e.getMessage());
                e.printStackTrace();
                return new RenderPageRequestHandler(new PageProvider(new ErrorPage()));
            }
        });
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return KassaPage.class;
    }

    @Override
    public Class<? extends Page> getLoginPage() {
        return LoginPage.class;
    }

    @Override
    protected Object getHiveKey() {
        return HIVE_KEY;
    }

    @Override
    protected void setUpHive() {
        PolicyFileHiveFactory factory = new SwarmPolicyFileHiveFactory(getActionFactory());
        factory.setAlias("kassa", KassaPage.class.getName());
        factory.setAlias("products", ProductPage.class.getName());
        factory.setAlias("transactions", TransactionPage.class.getName());
        factory.setAlias("overview", OverviewPage.class.getName());
        factory.addPolicyFile(getClass().getResource("/register.hive"));
        HiveMind.registerHive(getHiveKey(), factory);
    }

    @Override
    public Session newSession(Request request, Response response) {
        RegisterSession session = new RegisterSession(this, request);
        return session;
    }

    private void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Brussels"));
    }
}
