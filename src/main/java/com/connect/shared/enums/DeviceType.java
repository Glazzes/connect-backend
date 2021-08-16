package com.connect.shared.enums;

public enum DeviceType {
    PC("PC"),
    MOBILE("Mobile");

    String name;

    private DeviceType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
