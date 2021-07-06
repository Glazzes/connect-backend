package com.connect.configurations.security.utils.cookie;

import com.connect.exceptions.NotFoundException;

import javax.servlet.http.Cookie;
import java.util.Arrays;

public class CookieUtil {

    public static Cookie createCookieForType(CookieType type, String refreshToken){
        Cookie cookie = new Cookie(
                type.getName(), refreshToken
        );

        cookie.setHttpOnly(true);
        cookie.setPath(type.getPath());
        cookie.setSecure(false);
        cookie.setMaxAge(type.getDuration());

        return cookie;
    }

    public static Cookie getCookieByName(String name, Cookie[] cookies){
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = "There's no cookie with name " + name;
                    return new NotFoundException(errorMessage);
                });
    }

    public static Cookie restartCookieMaxAgeForType(CookieType type, Cookie cookie){
        cookie.setMaxAge(type.getDuration());
        return cookie;
    }

}