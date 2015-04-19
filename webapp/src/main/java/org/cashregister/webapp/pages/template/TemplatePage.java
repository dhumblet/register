package org.cashregister.webapp.pages.template;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.cookies.CookieUtils;
import org.cashregister.webapp.RegisterSession;
import org.cashregister.webapp.login.LoginHandler;
import org.cashregister.webapp.pages.kassa.KassaPage;
import org.cashregister.webapp.pages.login.LoginPage;
import org.cashregister.webapp.pages.product.ProductPage;
import org.cashregister.webapp.pages.overview.OverviewPage;
import org.cashregister.webapp.pages.transaction.TransactionPage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by derkhumblet on 10/11/14.
 */
public abstract class TemplatePage extends WebPage {
    private WebMarkupContainer navigation;

    public TemplatePage() {
        super();
        addStaticResource("pageStyleSheets", getPageSpecificStyleSheets(), "sheet", "href");
        addStaticResource("javascriptPagesImports", getPageSpecificJavaImports(), "import", "src");
        addNavigations();
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new Label("title", getTitle()));
        addMerchantHeader();
    }

    private void addMerchantHeader() {
        WebMarkupContainer merchantHeader = new WebMarkupContainer("merchantHeader") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(getRegisterSession().isUserAuthenticated());
            }
        };
        add(merchantHeader.setOutputMarkupId(true));

        merchantHeader.add(new Label("headerDate", getDate()));
        if (getRegisterSession().isUserAuthenticated()) {
            merchantHeader.add(new Label("merchantName", getRegisterSession().getUser().getMerchant().getName()));
        } else {
            merchantHeader.add(new Label("merchantName", ""));
        }

        merchantHeader.add(new Link<Void>("logoutLink") {
            @Override
            public void onClick() {
                Session.get().invalidate();
                new CookieUtils().remove(LoginHandler.REMEMBER_ME_TOKEN);
                setResponsePage(LoginPage.class);
            }

        });
    }

    protected abstract String getTitle();

    private String getDate() {
        return (new SimpleDateFormat("EEEE d MMMM yyyy", new Locale("nl", "BE"))).format(new Date());
    }

    private void addStaticResource(String container, List<String> references, final String childId, final String urlAttribute) {
        add(new ListView<String>(container, references) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String url;
                if (item.getModelObject().startsWith("http")) {
                    url = item.getModelObject();
                } else {
                    url = UrlUtils.rewriteToContextRelative(item.getModelObject(), RequestCycle.get());
                }
                item.add(new WebMarkupContainer(childId).add(AttributeModifier.append(urlAttribute, url)));
            }

        });
    }

    protected void addNavigations() {
        navigation = new WebMarkupContainer("navigation") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(getRegisterSession().isUserAuthenticated());
            }
        };

        add(navigation.setOutputMarkupId(true));
        addNavigation("kassa", KassaPage.class);
        addNavigation("products", ProductPage.class);
        addNavigation("transactions", TransactionPage.class);
        addNavigation("overview", OverviewPage.class);
    }

    private void addNavigation(String id, final Class<? extends Page> resultPage) {
        navigation.add(new AjaxLink<String>(id) {
            @Override
            public void onClick(AjaxRequestTarget target){
                setResponsePage(resultPage);
            }
        });
    }

    protected List<String> getPageSpecificStyleSheets() {
        return Arrays.asList(new String[]{
                "css/bootstrap.min.css",
                "css/plugins/dataTables.bootstrap.css",
                "css/plugins/metisMenu/metisMenu.min.css",
                "css/plugins/timeline.css",
                "css/sb-admin-2.css",
                "css/plugins/morris.css",
                "font-awesome-4.1.0/css/font-awesome.min.css",
                "css/custom.css"
        });
    }

    protected List<String> getPageSpecificJavaImports() {
        return Arrays.asList(new String[]{
                "js/jquery.js",
                "js/bootstrap.min.js",
                "js/plugins/metisMenu/metisMenu.min.js",
                "js/sb-admin-2.js",
                "js/shortcut.js",
                "js/custom.js"
        });
    }

    private RegisterSession getRegisterSession() {
        return (RegisterSession) getSession();
    }
}
