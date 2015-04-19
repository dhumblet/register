package org.cashregister.webapp.util;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by derkhumblet on 24/11/14.
 */
public class ProductHelperTest {

    @Test
    public void isProductTest() {
        Assert.assertTrue(ProductHelper.isProduct("1234567890123"));
        Assert.assertFalse(ProductHelper.isProduct("Krant"));
    }

    @Test
    public void isCategoryTest() {
        Assert.assertTrue(ProductHelper.isCategory("Krant"));
        Assert.assertFalse(ProductHelper.isCategory("1234567890123"));
    }
}
