package com.acabra.robot.bot;

import java.util.Locale;

public enum OsType {
    WIN, MAC, OTHER;

    public static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ROOT);

    public static OsType getOsType() {
        if(OS_NAME.contains("windows")) {
            return WIN;
        } else if(OS_NAME.contains("mac")) {
            return MAC;
        }
        return OTHER;
    }
}
