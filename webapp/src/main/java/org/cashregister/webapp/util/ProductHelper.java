package org.cashregister.webapp.util;

/**
 * Created by derkhumblet on 24/11/14.
 */
public final class ProductHelper {
//    private static final String PATTERN_PROD = "[0-9]{13}";
    private static final String PATTERN_PROD = "[0-9]+";
    private static final String PATTERN_CAT = "[a-zA-Z]*";

    public static boolean isProduct(final String identifier) {
        return identifier != null && identifier.matches(PATTERN_PROD);
    }

    public static boolean isCategory(final String identifier) {
        return identifier.matches(PATTERN_CAT);
    }
}
