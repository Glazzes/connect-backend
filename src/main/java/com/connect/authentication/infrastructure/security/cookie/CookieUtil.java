package com.connect.authentication.infrastructure.security.cookie;

import com.connect.shared.exception.generic.NotFoundException;

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

    public static void restartMaxAge(int  duration, Cookie cookie){
        cookie.setMaxAge(duration);
    }

}