package org.cashregister.webapp.login;

import java.io.Serializable;

/**
 * Created by derkhumblet on 16/01/15.
 */
public class LoginByCookie  implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;
    public static final int LENGTH = 3;
    private final String login, password;
    private final Integer ttl;

    public LoginByCookie(String login, String password, Integer ttl) {
        this.login = login;
        this.password = password;
        this.ttl = ttl;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getTtl() {
        return ttl;
    }


    public boolean isValid(int ttl) {
        return login != null
                && password != null
                && this.ttl != null && this.ttl.intValue() == ttl;
    }
}
