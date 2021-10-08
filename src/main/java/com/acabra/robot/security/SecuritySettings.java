package com.acabra.robot.security;

public class SecuritySettings {
    public final boolean lockOnChange;
    public final boolean panicMode;

    private SecuritySettings(boolean lockOnChange, boolean panicMode) {
        this.lockOnChange = lockOnChange;
        this.panicMode = panicMode;
    }

    public static SecuritySettings of(boolean lockOnChange, boolean panicMode) {
        return new SecuritySettings(lockOnChange, panicMode);
    }
}
