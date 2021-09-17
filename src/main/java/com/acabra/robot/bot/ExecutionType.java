package com.acabra.robot.bot;

public enum ExecutionType {
    MOUSE_MOVER, NOTEPAD_TYPE;

    public static ExecutionType getDefault() {
        return MOUSE_MOVER;
    }
}
