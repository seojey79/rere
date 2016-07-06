package com.seojey.domain.type;

import org.springframework.util.StringUtils;

/**
 * Created by kimjun on 2016. 5. 16..
 */
public enum Platform {
    /**
     * PC web 플랫폼
     */
    PC("PC"),
    /**
     * Android OS 프랫폼
     */
    AOS("AOS"),
    /**
     * iOS 플랫폼
     */
    IOS("IOS"),
    /**
     * Mobile web 플랫폼
     */
    MW("MW"),

    UNDEFINED("UNDEFINED");



    private String desc;
    private Platform(String scheme) {
        this.desc = scheme;
    }

    public String getDesc() {
        return desc;
    }

    public static Platform getPlatform(String desc) {
        if (StringUtils.isEmpty(desc)) {
            return UNDEFINED;
        }

        for (Platform type : Platform.values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }

        return UNDEFINED;
    }
}
