package org.cashregister.webapp.login;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.application.ComponentInstantiationListenerCollection;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.cookies.CookieUtils;
import org.apache.wicket.util.crypt.Base64;
import org.cashregister.webapp.RegisterSession;
import org.cashregister.webapp.pages.login.LoginPage;
import org.cashregister.webapp.security.api.EncryptService;
import org.cashregister.webapp.security.api.SecurityDetailsService;
import org.cashregister.webapp.security.impl.SecurityDetails;
import org.cashregister.webapp.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.security.authentication.LoginException;
import org.wicketstuff.security.hive.authentication.DefaultSubject;
import org.wicketstuff.security.hive.authentication.LoginContext;
import org.wicketstuff.security.hive.authentication.Subject;
import org.wicketstuff.security.hive.authorization.SimplePrincipal;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

/**
 * Created by derkhumblet on 16/01/15.
 */
public class LoginHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LoginPage.class);
    public static final String REMEMBER_ME_TOKEN = "merchantBOtoken";
    private static final String SEPARATOR = "KS293/16";
    private static final int ONE_MONTH = 2678400;
    private static SpringComponentInjector injector;

    @SpringBean
    private EncryptService encryptService;

    @SpringBean(name = "authenticationManager")
    private AuthenticationManager authenticationManager;


    @SpringBean
    private SecurityDetailsService securityDetailsService;

    @SpringBean
    private UserService userService;

    private final String email, password;

    public LoginHandler(String email, String password) {
        this.email = email;
        this.password = password;

        loadInjector();
        injector.inject(this);

        try {
            Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public boolean loginByCookie(Request request) {
        // Fetch cookie
        String value = getCookieUtils().load(REMEMBER_ME_TOKEN);
        if (StringUtils.isBlank(value)) {
            return false;
        }

        // Parse cookie to object
        final LoginByCookie loginByCookie = cookieToAuth(value);
        if (loginByCookie == null || !loginByCookie.isValid(ONE_MONTH)) {
            return false;
        }

        // Create auth token
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginByCookie.getLogin(), loginByCookie.getPassword());

        // Authenticate credentials
        LoginContext context = new LoginContext() {
            @Override
            public Subject login() throws LoginException {
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                DefaultSubject subject = new DefaultSubject();
                for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                    subject.addPrincipal(new SimplePrincipal(grantedAuthority.getAuthority()));
                }
                return subject;
            }
        };

        // Login with the context
        return login(context);
    }

    public boolean login(final boolean rememberMe) {
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        LoginContext context = new LoginContext() {
            @Override
            public Subject login() throws LoginException {
                Authentication authentication = authenticationManager.authenticate(token);
                if (rememberMe) {
                    String cookie = authToCookie((SecurityDetails) authentication.getPrincipal());
                    if (cookie != null) {
                        getCookieUtils().save(REMEMBER_ME_TOKEN, cookie);
                    }
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
                DefaultSubject subject = new DefaultSubject();
                for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                    subject.addPrincipal(new SimplePrincipal(grantedAuthority.getAuthority()));
                }
                return subject;
            }
        };

        return login(context);
    }

    private boolean login(final LoginContext context) {
        RegisterSession waspSession = (RegisterSession) Session.get();
        try {
            waspSession.login(context);
            putSessionData(waspSession, email);
            waspSession.bind();
            return true;
        } catch (Exception e) {
            waspSession.logoff(context);
            if (!(e instanceof BadCredentialsException)) {
                LOG.error("Login failed", e);
            }
            return false;
        }
    }

    private String authToCookie(SecurityDetails details) {
        LoginByCookie loginByCookie = new LoginByCookie(
                details.getUsername(),
                details.getPassword(),
                ONE_MONTH);
        try {
            return encode(encrypt(loginByCookie));
        } catch (IOException e) {
            return null;
        }
    }

    private LoginByCookie cookieToAuth(String value) {
        try {
            return decrypt(decode(value));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new LoginByCookie(null, null, null);
        }
    }

    private String encrypt(LoginByCookie loginByCookie) {
        final StringBuilder cookieToken = new StringBuilder();
        cookieToken.append(loginByCookie.getLogin())
                .append(SEPARATOR)
                .append(loginByCookie.getPassword())
                .append(SEPARATOR)
                .append(loginByCookie.getTtl());

        return encryptService.encrypt(cookieToken.toString());
    }

    private LoginByCookie decrypt(ByteArrayInputStream bais) throws ClassNotFoundException, IOException {
        ObjectInputStream o = new ObjectInputStream(bais);

        String cookie = encryptService.decrypt(((String) o.readObject()).getBytes());
        String[] cookieValues = cookie.split(SEPARATOR);
        if (cookieValues.length == LoginByCookie.LENGTH) {
            return new LoginByCookie(cookieValues[0], cookieValues[1], Integer.valueOf(cookieValues[2]));
        }
        return new LoginByCookie(null, null, null);
    }

    private String encode(String s) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(s);
        return new String(Base64.encodeBase64(baos.toByteArray()));
    }

    private ByteArrayInputStream decode(String s) {
        return new ByteArrayInputStream(Base64.decodeBase64(s));
    }

    private void putSessionData(RegisterSession waspSession, String email) {
        SecurityDetails details = securityDetailsService.getCurrentUser();
        waspSession.setUser(userService.findByName(details.getUsername()));
        waspSession.setLocale(details.getLocale());
    }

    private static synchronized void loadInjector() {
        if (injector != null) {
            return;
        }

        final ComponentInstantiationListenerCollection listeners = Application.get().getComponentInstantiationListeners();
        final Iterator<IComponentInstantiationListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            final IComponentInstantiationListener next = iterator.next();
            if (next instanceof SpringComponentInjector) {
                injector = (SpringComponentInjector) next;
                break;
            }
        }
    }

    private CookieUtils getCookieUtils() {
        CookieUtils cookieUtils = new CookieUtils();
        cookieUtils.getSettings().setMaxAge(ONE_MONTH);
        return cookieUtils;
    }
}
