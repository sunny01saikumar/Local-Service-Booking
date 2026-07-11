package com.sai.geoLocation.util;

import org.apache.commons.lang3.StringUtils;

public final class SlugUtil {
    private SlugUtil() {}

    public static String toSlug(String value) {
        return StringUtils.defaultString(value).trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
}
