package org.cashregister.webapp.service;

import org.cashregister.webapp.service.api.MerchantService;
import org.cashregister.webapp.service.api.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by derkhumblet on 11/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:testContext.xml" })
public abstract class AbstractServiceTest {
    @Autowired protected MerchantService merchantService;
    @Autowired protected UserService userService;
}
