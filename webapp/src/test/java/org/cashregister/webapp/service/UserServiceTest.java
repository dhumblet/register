package org.cashregister.webapp.service;

import junit.framework.Assert;
import org.cashregister.domain.Merchant;
import org.cashregister.domain.User;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by derkhumblet on 12/01/15.
 */
@Ignore
public class UserServiceTest extends AbstractServiceTest {

    @Test @Transactional
    public void createRootUserTest() {
        User user = userService.createUser("root", "darkside", merchantService.createMerchant("Test Merchant"), null);
        System.out.println(user.getId() + " " + user.getLogin() + " " + user.getPassword());
    }

    @Test @Transactional
    public void createHildeUserTest() {
        userService.createUser("humblethilde@gmail.com", "hilde", merchantService.createMerchant("Test Merchant"), null);
        User user = userService.createUser("humblethilde@gmail.com", "hilde", merchantService.createMerchant("Test Merchant"), null);
        System.out.println(user.getId() + " " + user.getLogin() + " " + user.getPassword());
    }

    @Test @Transactional
    public void createDieteUserTest() {
        User user = userService.createUser("dietehumblet@gmail.com", "diete", merchantService.createMerchant("Test Merchant"), null);
        System.out.println(user.getId() + " " + user.getLogin() + " " + user.getPassword());
    }



    @Test @Transactional @Ignore
    public void createUserTest() {
        String login = "login";
        Merchant merchant = merchantService.createMerchant("Test Merchant");
        User user = userService.createUser(login, "password", merchant, null);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(user.getMerchant());
        Assert.assertNotNull(user.getLogin());
        Assert.assertEquals(login, user.getLogin());
        Assert.assertFalse(user.isLocked());
        Assert.assertNotNull(user.getPassword());
    }

    @Test @Transactional  @Ignore
    public void differentHasedPasswordsTest() {
        Merchant merchant = merchantService.createMerchant("Test Merchant");
        User user1 = userService.createUser("login", "password", merchant, null);
        User user2 = userService.createUser("login", "password", merchant, null);
        Assert.assertNotNull(user1);
        Assert.assertNotNull(user2);
        Assert.assertNotNull(user1.getPassword());
        Assert.assertNotNull(user2.getPassword());
        Assert.assertFalse(user1.getPassword().equals(user2.getPassword()));
    }
}
