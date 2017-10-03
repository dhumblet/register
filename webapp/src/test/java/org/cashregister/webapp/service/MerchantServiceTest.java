package org.cashregister.webapp.service;

import junit.framework.Assert;
import org.cashregister.domain.Merchant;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by derkhumblet on 11/01/15.
 */
public class MerchantServiceTest extends AbstractServiceTest {

    @Test @Transactional
    @Ignore
    public void createMerchantTest() {
        String name = "Test Merchant";
        Merchant m = merchantService.createMerchant(name);
        Assert.assertNotNull(m);
        Assert.assertNotNull(m.getId());
        Assert.assertEquals(name, m.getName());
    }

}
