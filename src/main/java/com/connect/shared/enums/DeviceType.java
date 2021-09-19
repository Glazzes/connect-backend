package com.connect.shared.enums;

public enum DeviceType {
    DESKTOP("PC"),
    MOBILE_BROWSER("MobileBrowser"),
    MOBILE_APP("Mobile");

    String name;

    private DeviceType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}