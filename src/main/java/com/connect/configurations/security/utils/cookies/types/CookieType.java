package com.connect.configurations.security.utils.cookies.types;

public enum CookieType {
    REFRESH("RefreshToken", 3600 * 24 * 7, "/");

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
