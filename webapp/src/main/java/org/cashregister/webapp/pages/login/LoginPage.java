package org.cashregister.webapp.pages.login;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.cashregister.webapp.component.CustomFeedbackPanel;
import org.cashregister.webapp.login.EmailValidator;
import org.cashregister.webapp.login.LoginHandler;
import org.cashregister.webapp.pages.kassa.KassaPage;
import org.cashregister.webapp.pages.template.TemplatePage;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derkhumblet on 16/01/15.
 */
public class LoginPage extends TemplatePage {
    private String email, password;
    private boolean rememberMe;

    @Override
    public String getTitle() { return "Login"; }

    public LoginPage(PageParameters params) {
        addLoginForm();
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        if (new LoginHandler(email, password).loginByCookie(getRequest())) {
            continueToOriginalDestination();
            setResponsePage(KassaPage.class);
        }
    }

    @Override
    public boolean isVersioned() {
        return false;
    }

    private void addLoginForm() {
        StatelessForm<LoginPage> credentials = new StatelessForm<>("credentials", new CompoundPropertyModel<>(this));
        add(credentials);

        EmailTextField emailTextField = new EmailTextField("email"); //, EmailValidator.getInstance());
        emailTextField.setRequired(true); //.add(JQueryFocusBehavior.focus());
        credentials.add(emailTextField);
        credentials.add(new PasswordTextField("password"));
        credentials.add(new LoginBtn("login"));
        final CustomFeedbackPanel feedback = new CustomFeedbackPanel("feedback", new ContainerFeedbackMessageFilter(credentials));
        credentials.add(feedback.setOutputMarkupId(true));
//        credentials.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(this, "rememberMe")));
    }

    class LoginBtn extends Button {

        private static final long serialVersionUID = 1L;

        public LoginBtn(String id) {
            super(id);
        }

        @Override
        public void onSubmit() {
            super.onSubmit();
            if (new LoginHandler(email, password).login(rememberMe)) {
                continueToOriginalDestination();
                setResponsePage(KassaPage.class);
            } else {
                password = null;
                error(new StringResourceModel("error.login.failed", this, null).getString());
            }
        }

        @Override
        public void onError() {
            super.onError();

        }
    }
}
