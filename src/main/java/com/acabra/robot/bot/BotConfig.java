package com.acabra.robot.bot;

import com.acabra.robot.util.ComboBoxPanel;
import com.acabra.robot.util.SecureFieldPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.*;
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
    public final Map<String, String> executionMap;
    public final boolean lockOnChange;
    public final boolean panicMode;

    public BotConfig(String loopingText, long runningTime, TimeUnit timeUnit, ExecutionType exType,
                     OnFinishAction onFinishAction, boolean lockOnChange, boolean panicMode, Map<String, String> map) {
        this.loopingText = loopingText;
        this.runningTime = runningTime;
        this.timeUnit = timeUnit;
        this.onFinishAction = onFinishAction;
        this.executionType = exType;
        this.lockOnChange = lockOnChange;
        this.panicMode = panicMode;
        this.executionMap = Collections.unmodifiableMap(map);
        log.info(toString());
    }

    public BotConfig() {
        this.timeUnit = TimeUnit.SECONDS;
        this.loopingText = DEFAULT_EXECUTION_TEXT;
        this.runningTime = DEFAULT_LOOPING_TIME_SECS;
        this.onFinishAction = OnFinishAction.NOTHING;
        this.lockOnChange = false;
        this.panicMode = true;
        this.executionMap = Collections.emptyMap();
        this.executionType = ExecutionType.getDefault();
    }

    public static BotConfig fromArgs(String[] args) {
        if(args == null || args.length == 0) {
            log.debug("No args given at launch");
            if(getPredefinedConfig()) {
                PredefBot preDefBotType = getPredefinedBotType();
                return botConfigForName(preDefBotType);
            }
            ExecutionType executionType = getExecutionType();
            String text = getLoopingText(executionType);
            TimeUnit timeUnit = getTimeUnit();
            long runningTime = getRunningTime(timeUnit);
            OnFinishAction finishAction = getFinishAction();
            Map<String, String> map = buildExecutionMap();
            boolean lockOnChange = isLockOnChange();
            boolean panicMode = isPanicMode(lockOnChange);
            return new BotConfig(text, runningTime, timeUnit, executionType, finishAction, lockOnChange, panicMode, map);
        } else if(args.length == 1) {
            String name = args[0].toUpperCase(Locale.ROOT);
            return botConfigForName(PredefBot.valueOf(name));
        }
        log.debug("Args given dismissed {}, launching with defaults", Arrays.toString(args));
        return new BotConfig();
    }

    private static PredefBot getPredefinedBotType() {
        return ComboBoxPanel.getPredefinedBotType("Which Bot?", null);
    }

    private static boolean getPredefinedConfig() {
        return "Yes".equals(ComboBoxPanel.getNoYes("Predefined Bot?", "No"));
    }

    private static BotConfig botConfigForName(PredefBot type) {
        if(null == type) {
            return new BotConfig();
        }
        switch (type) {
            case HOME:
                log.info("Building home bot ...");
                return BotConfig.ofHome();
            case LUNCH:
                log.info("Config for Lunch ...");
                return BotConfig.ofLunch();
            case BREAK:
                log.info("Config for break ...");
                return BotConfig.ofBreak();
            case BREAK_PANIC:
                log.info("Config for break ...");
                return BotConfig.ofBreakPanic();
            case BACKGROUND:
                log.info("Config for break ...");
                return BotConfig.ofBackground();
            default:
                throw new NoSuchElementException("Unable to find the given value: " + type.name());
        }
    }

    private static BotConfig ofBackground() {
        return new BotConfig("", 8,
                TimeUnit.HOURS,
                ExecutionType.MOUSE_MOVER,
                OnFinishAction.LOCK, false, false, Collections.emptyMap());
    }

    private static BotConfig ofBreakPanic() {
        return new BotConfig("", getRunningTime(TimeUnit.MINUTES),
                TimeUnit.MINUTES,
                ExecutionType.MOUSE_MOVER,
                OnFinishAction.LOCK, true, true, Collections.emptyMap());
    }

    private static BotConfig ofBreak() {
        return new BotConfig("", getRunningTime(TimeUnit.MINUTES),
                TimeUnit.MINUTES,
                ExecutionType.MOUSE_MOVER,
                OnFinishAction.LOCK, true, false, Collections.emptyMap());
    }

    private static BotConfig ofLunch() {
        return new BotConfig("", 2,
                TimeUnit.HOURS,
                ExecutionType.MOUSE_MOVER,
                OnFinishAction.LOCK, true, false, Collections.emptyMap());
    }

    private static BotConfig ofHome() {
        return new BotConfig("", getRunningTime(TimeUnit.MINUTES),
                TimeUnit.MINUTES,
                ExecutionType.MOUSE_MOVER,
                OnFinishAction.HIBERNATE, true, false, Collections.emptyMap());
    }

    private static boolean isPanicMode(boolean lockOnChange) {
        return lockOnChange
                && "Yes".equals(ComboBoxPanel.getNoYes("Block station if changes detected?", "Yes"));
    }

    private static boolean isLockOnChange() {
        return !"Yes".equalsIgnoreCase(getRunInBackGround());
    }

    private static String getRunInBackGround() {
        return ComboBoxPanel.getNoYes("Will you use the computer while this runs ?", "Yes");
    }

    private static OnFinishAction getFinishAction() {
        return ComboBoxPanel.getFinishAction("Choose action after finishing ... ",
                OnFinishAction.NOTHING);
    }

    private static long getRunningTime(TimeUnit timeUnit) {
        return ComboBoxPanel.getRunningTime(timeUnit, DEFAULT_LOOPING_TIME_SECS);
    }

    private static TimeUnit getTimeUnit() {
        return ComboBoxPanel.getTimeUnit("Choose the running time units ... ", TimeUnit.SECONDS);
    }

    private static String getLoopingText(ExecutionType executionType) {
        return executionType == ExecutionType.NOTEPAD_TYPE ? getLoopingText() : "";
    }

    private static ExecutionType getExecutionType() {
        return ComboBoxPanel.getExecutionType("Choose the execution type... ", ExecutionType.NOTEPAD_TYPE);
    }

    private static Map<String, String> buildExecutionMap() {
        if("yes".equalsIgnoreCase(ComboBoxPanel.getNoYes("do you want variables?", "No"))) {
            int count = 0;
            Map<String, String> map = new HashMap<>();
            while(true) {
                String key = JOptionPane.showInputDialog("Key_" + count);
                if(key == null || key.isBlank()) break;
                String value = new SecureFieldPanel("Value_" + count).password.orElse(null);
                if(value == null || value.isBlank()) break;
                map.put(key, value);
                ++count;
            }
            return map;
        }
        return Collections.emptyMap();
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
