package com.acabra.robot.bot;

import com.acabra.robot.util.ComboBoxPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BotConfig {
    private static final int DEFAULT_LOOPING_TIME_SECS = 120;
    public static final String DEFAULT_EXECUTION_TEXT = "Hi I Am working hard";

    public final String loopingText;
    public final long runningTime;
    public final TimeUnit timeUnit;

    public BotConfig(String loopingText, long runningTime, TimeUnit timeUnit) {
        this.loopingText = loopingText;
        this.runningTime = runningTime;
        this.timeUnit = timeUnit;
    }

    public BotConfig() {
        this.timeUnit = TimeUnit.SECONDS;
        this.loopingText = DEFAULT_EXECUTION_TEXT;
        this.runningTime = DEFAULT_LOOPING_TIME_SECS;
    }

    public static BotConfig fromArgs(String... args) {
        if(args == null || args.length == 0) {
            log.debug("No args given at launch");
            String text = getLoopingText();
            TimeUnit timeUnit = ComboBoxPanel.getTimeUnit();
            long runningTime = getRunningTime(timeUnit);
            return new BotConfig(text, runningTime, timeUnit);
        } else if(args.length >= 2) {
            log.debug("two arg given {} ", Arrays.toString(args));
            return new BotConfig(args[0], Long.parseLong(args[1]), TimeUnit.SECONDS);
        }
        log.debug("Args given dismissed {}, launching with defaults", Arrays.toString(args));
        return new BotConfig();
    }

    private static long getRunningTime(TimeUnit timeUnit) {
        String prompt = String.format("How much time in %s ...", timeUnit.toString().toLowerCase(Locale.ROOT));
        String text = JOptionPane.showInputDialog(prompt);
        try {
            if (text != null) {
                return Long.parseLong(text);
            }
        } catch (Exception e) {
            log.error("error {}", e.getMessage(), e);
        }
        return DEFAULT_LOOPING_TIME_SECS;
    }

    private static String getLoopingText() {
        String inputText = JOptionPane.showInputDialog("What is the looping text?");
        return inputText == null || inputText.isBlank() ? DEFAULT_EXECUTION_TEXT : inputText;
    }
}
