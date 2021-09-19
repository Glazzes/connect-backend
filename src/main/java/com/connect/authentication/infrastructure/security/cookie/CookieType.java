package com.connect.authentication.infrastructure.security.cookie;

public enum CookieType {
    REFRESH_TOKEN("RefreshToken", 3600 * 24 * 7, "/"),
    SESSION_SCOPED_REFRESH_TOKEN("RefreshToken", -1, "/");

    private final String name;
    private final int duration;
    private final String path;

    CookieType(String name, int value, String path){
        this.name = name;
        this.duration = value;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getPath(){
        return path;
    }
}
