package com.acabra.robot;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImprovedBot extends Robot implements Runnable {

    private static final List<Integer> DEFAULT_NOT_FOUND_KEY = List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_3);
    private static final long DEFAULT_TYPING_DELAY = 80L;
    public static final String DEFAULT_EXECUTION_TEXT = "Hi I Am working hard";
    private static final int DEFAULT_LOOPING_TIME_SECS = 120;
    private static Map<Character, List<Integer>> CHAR_EVT_MAP = buildCharEvtMap();
    private volatile boolean finish = false;

    private static final long DEFAULT_ACTION_DELAY = 500L;
    private static final long LOOP_SLEEP = 3000L;
    private final Dimension screenSize;
    private final String os;
    private final String text;

    //static { System.loadLibrary("lib/com_acabra_robot_ImprovedBot"); }

    public static void main(String... args) throws AWTException {
        if(args != null && args.length == 2) {
            String text = args[0] != null && args[0].length() > 0 ? args[0] :  null;
            int secondsRunning = Math.abs(Integer.parseInt(args[1]));
            ImprovedBot.start(text, secondsRunning);
            return;
        }
        ImprovedBot.start();
    }

    private static void start(String text, int secondsRunning) throws AWTException {
        ImprovedBot bot = new ImprovedBot(text);
        execute(bot, secondsRunning);
    }

    private static void start() throws AWTException {
        ImprovedBot bot = new ImprovedBot();
        execute(bot, DEFAULT_LOOPING_TIME_SECS);
    }

    private static void execute(ImprovedBot bot, int secondsRunning) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(bot, executor);
        CompletableFuture.runAsync(()-> {
            bot.requestShutdown();
            executor.shutdown();
        }, CompletableFuture.delayedExecutor(secondsRunning, TimeUnit.SECONDS));
    }

    public ImprovedBot() throws AWTException {
        super();
        this.text = DEFAULT_EXECUTION_TEXT;
        this.screenSize = Toolkit. getDefaultToolkit(). getScreenSize();
        this.os = System.getProperty("os.name");
    }

    public ImprovedBot(String text) throws AWTException {
        this.text = text == null || text.length() == 0 ? DEFAULT_EXECUTION_TEXT : text;
        this.screenSize = Toolkit. getDefaultToolkit(). getScreenSize();
        this.os = System.getProperty("os.name");
    }

    @Override
    public void run() {
        try {
            int offset = 0;
            runCommand("notepad");
            pressCtrlWith(KeyEvent.VK_N);
            while (continueRunning()) {
                type(text);
                deleteAll();
                Thread.sleep(LOOP_SLEEP);
            }
            deleteAll();
            closeProgram();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void pressCtrlWith(int evtKey) {
        pressCombined(KeyEvent.VK_CONTROL, evtKey);
    }

    public boolean continueRunning() {
        return !finish;
    }

    public void requestShutdown() {
        finish = true;
    }

    private void closeProgram() throws InterruptedException {
        pressCombined(KeyEvent.VK_ALT, KeyEvent.VK_F4);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    private void deleteAll() throws InterruptedException {
        pressCombined(KeyEvent.VK_CONTROL, KeyEvent.VK_A);
        pressRelease(KeyEvent.VK_BACK_SPACE);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    private void pressCombined(int groupEvtKey, int terminalEvtKey) {
        keyPress(groupEvtKey);
        pressRelease(terminalEvtKey);
        keyRelease(groupEvtKey);
    }

    private void pressRelease(int evtKey) {
        keyPress(evtKey);
        keyRelease(evtKey);
    }

    private void execute(String command) throws InterruptedException {
        type(command);
        typeEnter();
    }

    private native String getKeyboardLanguage();

    private void runCommand(String command) throws InterruptedException {
        minimizeAll();
        openRunWindow();
        execute(command);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    private void openRunWindow() throws InterruptedException {
        pressCombined(KeyEvent.VK_WINDOWS, KeyEvent.VK_R);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    private void typeEnter() {
        pressRelease(KeyEvent.VK_ENTER);
    }

    private void leftClick() {
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void RightClick() {
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void minimizeAll() throws InterruptedException {
        keyPress(KeyEvent.VK_WINDOWS);
        type('m');
        keyRelease(KeyEvent.VK_WINDOWS);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    private void type(String text) throws InterruptedException {
        char[] chars = text.toLowerCase().toCharArray();
        for (char aChar : chars) {
            type(aChar);
        }
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    private void type(char aChar) throws InterruptedException {
        List<Integer> ev = CHAR_EVT_MAP.getOrDefault(aChar, DEFAULT_NOT_FOUND_KEY);
        if (ev.size() == 1) {
            pressRelease(ev.get(0));
        } else {
            pressCombined(ev.get(0), ev.get(1));
        }
        Thread.sleep(DEFAULT_TYPING_DELAY);
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
            put('!', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1));
            put(' ', List.of(KeyEvent.VK_SPACE));
            put('?', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE));
            put('+', List.of(KeyEvent.VK_PLUS));
        }};
    }
}
