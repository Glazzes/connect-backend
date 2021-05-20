package com.connect.configurations.security.utils.cookies;

import com.connect.configurations.security.utils.cookies.types.CookieType;

import javax.servlet.http.Cookie;

public class CookieSecurityUtil {

    public static Cookie createAuthenticationCookieForType(CookieType type, String refreshToken){
        Cookie cookie = new Cookie(
                type.getName(), refreshToken
        );

        cookie.setHttpOnly(true);
        cookie.setPath(type.getPath());
        cookie.setSecure(false);
        cookie.setMaxAge(type.getDuration());

        return cookie;
    }

}