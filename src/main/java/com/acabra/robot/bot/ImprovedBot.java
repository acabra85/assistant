package com.acabra.robot.bot;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class ImprovedBot extends Robot implements Runnable {

    protected static final java.util.List<Integer> DEFAULT_NOT_FOUND_KEY = List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_3);
    protected static final long LOOP_SLEEP = 3000L;
    private static final long DEFAULT_TYPING_DELAY = 80L;
    protected static final long DEFAULT_ACTION_DELAY = 500L;

    private volatile boolean finish = false;

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final OsType os;
    protected final String loopText;
    private final static Map<Character, List<Integer>> CHAR_EVT_MAP = Collections.unmodifiableMap(buildCharEvtMap());

    public ImprovedBot(OsType os, String loopText) throws AWTException {
        super();
        this.loopText = loopText;
        this.os = os;
    }

    protected void type(char aChar) throws InterruptedException {
        List<Integer> ev = CHAR_EVT_MAP.getOrDefault(aChar, DEFAULT_NOT_FOUND_KEY);
        if (ev.size() == 1) {
            pressRelease(ev.get(0));
        } else {
            pressCombined(ev.get(0), ev.get(1));
        }
        Thread.sleep(DEFAULT_TYPING_DELAY);
    }

    protected void typeText(String text) throws InterruptedException {
        char[] chars = text.toLowerCase().toCharArray();
        for (char aChar : chars) {
            type(aChar);
        }
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    protected void pressCombined(int groupEvtKey, int terminalEvtKey) {
        keyPress(groupEvtKey);
        pressRelease(terminalEvtKey);
        keyRelease(groupEvtKey);
    }

    protected void pressRelease(int evtKey) {
        keyPress(evtKey);
        keyRelease(evtKey);
    }

    protected void execute(String command) throws InterruptedException {
        typeText(command);
        typeEnter();
    }

    protected void typeEnter() {
        pressRelease(KeyEvent.VK_ENTER);
    }

    protected void leftClick() {
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    protected void rightClick() {
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    protected void pressCtrlWith(int evtKey) {
        pressCombined(KeyEvent.VK_CONTROL, evtKey);
    }

    protected boolean continueRunning() {
        return !finish;
    }

    protected void requestShutdown() {
        finish = true;
    }

    protected abstract void openRunWindow() throws InterruptedException;

    protected abstract void minimizeAll() throws InterruptedException;

    protected Logger getLog() {
        return log;
    }
    private static HashMap<Character, List<Integer>> buildCharEvtMap() {
        return new HashMap<>(){{
            put('a', List.of(KeyEvent.VK_A));
            put('b', List.of(KeyEvent.VK_B));
            put('c', List.of(KeyEvent.VK_C));
            put('d', List.of(KeyEvent.VK_D));
            put('e', List.of(KeyEvent.VK_E));
            put('f', List.of(KeyEvent.VK_F));
            put('g', List.of(KeyEvent.VK_G));
            put('h', List.of(KeyEvent.VK_H));
            put('i', List.of(KeyEvent.VK_I));
            put('j', List.of(KeyEvent.VK_J));
            put('k', List.of(KeyEvent.VK_K));
            put('l', List.of(KeyEvent.VK_L));
            put('m', List.of(KeyEvent.VK_M));
            put('n', List.of(KeyEvent.VK_N));
            put('o', List.of(KeyEvent.VK_O));
            put('p', List.of(KeyEvent.VK_P));
            put('q', List.of(KeyEvent.VK_Q));
            put('r', List.of(KeyEvent.VK_R));
            put('s', List.of(KeyEvent.VK_S));
            put('t', List.of(KeyEvent.VK_T));
            put('u', List.of(KeyEvent.VK_U));
            put('v', List.of(KeyEvent.VK_V));
            put('w', List.of(KeyEvent.VK_W));
            put('x', List.of(KeyEvent.VK_X));
            put('y', List.of(KeyEvent.VK_Y));
            put('z', List.of(KeyEvent.VK_Z));
            put('A', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_A));
            put('B', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_B));
            put('C', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_C));
            put('D', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_D));
            put('E', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_E));
            put('F', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_F));
            put('G', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_G));
            put('H', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_H));
            put('I', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_I));
            put('J', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_J));
            put('K', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_K));
            put('L', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_L));
            put('M', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_M));
            put('N', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_N));
            put('O', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_O));
            put('P', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_P));
            put('Q', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_Q));
            put('R', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_R));
            put('S', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_S));
            put('T', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_T));
            put('U', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_U));
            put('V', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_V));
            put('W', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_W));
            put('X', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_X));
            put('Y', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_Y));
            put('Z', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_Z));
            put('0', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_0));
            put('1', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1));
            put('2', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_2));
            put('3', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_3));
            put('4', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_4));
            put('5', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_5));
            put('6', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_6));
            put('7', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_7));
            put('8', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_8));
            put('9', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_9));
            put(' ', List.of(KeyEvent.VK_SPACE));
            put('+', List.of(KeyEvent.VK_PLUS));
            put('-', List.of(KeyEvent.VK_MINUS));
            put('_', List.of(KeyEvent.VK_UNDERSCORE));
            put('.', List.of(KeyEvent.VK_PERIOD));
        }};
    }

    protected abstract void fileNew();

    protected abstract void closeProgram(boolean sleep) throws InterruptedException;

    protected abstract void deleteAll(boolean sleep) throws InterruptedException;
}