package com.acabra.robot.bot;

import com.acabra.robot.util.ComboBoxPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BotConfig {
    private static final int DEFAULT_LOOPING_TIME_SECS = 120;
    public static final String DEFAULT_EXECUTION_TEXT = "Hi I Am working hard";

    public final String loopingText;
    public final long runningTime;
    public final TimeUnit timeUnit;
    public final OnFinishAction onFinishAction;
    public final ExecutionType executionType;

    public BotConfig(String loopingText, long runningTime, TimeUnit timeUnit,ExecutionType exType, OnFinishAction onFinishAction) {
        this.loopingText = loopingText;
        this.runningTime = runningTime;
        this.timeUnit = timeUnit;
        this.onFinishAction = onFinishAction;
        this.executionType = exType;
        log.info(toString());
    }

    public BotConfig() {
        this.timeUnit = TimeUnit.SECONDS;
        this.loopingText = DEFAULT_EXECUTION_TEXT;
        this.runningTime = DEFAULT_LOOPING_TIME_SECS;
        this.onFinishAction = OnFinishAction.NOTHING;
        executionType = ExecutionType.getDefault();
    }

    public static BotConfig fromArgs(String... args) {
        if(args == null || args.length == 0) {
            log.debug("No args given at launch");
            ExecutionType executionType = ComboBoxPanel.getExecutionType("Choose the execution type... ", ExecutionType.NOTEPAD_TYPE);
            String text = executionType == ExecutionType.NOTEPAD_TYPE ? getLoopingText() : "";
            TimeUnit timeUnit = ComboBoxPanel.getTimeUnit("Choose the running time units ... ", TimeUnit.SECONDS);
            long runningTime = ComboBoxPanel.getRunningTime(timeUnit, DEFAULT_LOOPING_TIME_SECS);
            OnFinishAction finishAction = ComboBoxPanel.getFinishAction("Choose action after finishing ... ",
                    OnFinishAction.NOTHING);
            return new BotConfig(text, runningTime, timeUnit, executionType, finishAction);
        } else if(args.length >= 2) {
            log.debug("two arg given {} ", Arrays.toString(args));
            return new BotConfig(args[0], Long.parseLong(args[1]), TimeUnit.SECONDS, ExecutionType.MOUSE_MOVER, OnFinishAction.NOTHING);
        }
        log.debug("Args given dismissed {}, launching with defaults", Arrays.toString(args));
        return new BotConfig();
    }

    private static String getLoopingText() {
        String inputText = JOptionPane.showInputDialog("What is the looping text?");
        return inputText == null || inputText.isBlank() ? DEFAULT_EXECUTION_TEXT : inputText;
    }

    @Override
    public String toString() {
        return "BotConfig{" +
                "loopingText='" + loopingText + '\'' +
                ", runningTime=" + runningTime +
                ", timeUnit=" + timeUnit +
                ", onFinishAction=" + onFinishAction +
                '}';
    }
}
