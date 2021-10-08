package com.acabra.robot.bot;

import com.acabra.robot.exception.UnexpectedSystemManipulationException;
import com.acabra.robot.security.SecurityMonitor;
import com.acabra.robot.security.SecuritySettings;
import lombok.extern.slf4j.Slf4j;

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
    protected static final long LOOP_SLEEP = 5000L;
    private static final long DEFAULT_TYPING_DELAY = 80L;
    protected static final long DEFAULT_ACTION_DELAY = 150L;
    private final OnFinishAction onFinishAction;
    protected final ExecutionType executionType;
    protected final Map<String, String> executionVariables;
    private final SecurityMonitor lockMonitor;

    private volatile boolean finish = false;

    protected final String loopText;
    protected final static Map<Character, List<Integer>> CHAR_EVT_MAP = Collections.unmodifiableMap(buildCharEvtMap());

    public ImprovedBot(String loopText, ExecutionType executionType, OnFinishAction onFinishAction,
                       SecuritySettings securitySettings, Map<String, String> executionVariables) throws AWTException {
        super();
        this.loopText = loopText;
        this.onFinishAction = onFinishAction;
        this.executionType = executionType;
        this.setAutoDelay((int)DEFAULT_ACTION_DELAY);
        this.setAutoWaitForIdle(true);
        this.executionVariables = executionVariables;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.lockMonitor = new SecurityMonitor(screenSize, securitySettings, MouseInfo.getPointerInfo(), this::getPixelColor);
    }

    protected void type(char aChar) throws InterruptedException {
        List<Integer> ev = CHAR_EVT_MAP.getOrDefault(aChar, DEFAULT_NOT_FOUND_KEY);
        if (ev.size() == 1) {
            keyStroke(ev.get(0));
        } else {
            pressCombined(ev.get(0), ev.get(1));
        }
        Thread.sleep(DEFAULT_TYPING_DELAY);
    }

    protected void typeText(String text) throws InterruptedException {
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            type(aChar);
        }
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    protected void pressCombined(int groupEvtKey, int terminalEvtKey) {
        keyPress(groupEvtKey);
        keyStroke(terminalEvtKey);
        keyRelease(groupEvtKey);
    }

    protected void pressCombined(List<Integer> combine) {
        switch (combine.size()) {
            case 2:
                pressCombined(combine.get(0), combine.get(1));
                break;
            case 3:
                pressCombined(combine.get(0), combine.get(1), combine.get(2));
                break;
            default:
                break;
        }
    }

    protected void pressCombined(int groupEvt1, int groupEvt2, int terminalEvtKey) {
        keyPress(groupEvt1);
        pressCombined(groupEvt2, terminalEvtKey);
        keyRelease(groupEvt1);
    }

    protected void keyStroke(int evtKey) {
        keyPress(evtKey);
        keyRelease(evtKey);
    }

    protected void keyStroke(List<Integer> events) {
        if(events.size() == 1) {
            keyStroke(events.get(0));
            return;
        }
        pressCombined(events);
    }

    protected void pressEsc() {
        keyStroke(KeyEvent.VK_ESCAPE);
    }

    protected void execute(String command) throws InterruptedException {
        typeText(command);
        typeEnter();
    }

    protected void typeEnter() {
        keyStroke(KeyEvent.VK_ENTER);
    }

    protected void leftClick() {
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    protected void rightClick() {
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    protected void runCommand(String command) throws InterruptedException {
        //minimizeAll();
        openRunWindow();
        execute(command);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @SuppressWarnings("BusyWait")
    protected void mouseMoverAction() throws InterruptedException {
        boolean cycle = false;
        PointerInfo pointerInfo;
        Point curLocation;
        while(continueRunning()) {
            pointerInfo = MouseInfo.getPointerInfo();
            if (pointerInfo != null) {
                curLocation = pointerInfo.getLocation();
                if(lockMonitor.shouldLockStation(curLocation))  {
                    log.error(">>>>>>>>>>Conditions Changed - LOCKING STATION <<<<<<<<<<<<<");
                    throw new UnexpectedSystemManipulationException("Initial Conditions changed");
                }
                mouseMove(curLocation.x, curLocation.y + (cycle ? 1 : -1) );
            } else {
                log.info("pointerInfo is null");
                pressEsc();
                pressEsc();
            }
            cycle = !cycle;
            Thread.sleep(3000L);
        }
    }

    protected void lockUnlock() throws InterruptedException {
        lockStation();
        unlockStation();
        Thread.sleep(10000L);
    }

    protected abstract void unlockStation();

    protected abstract void lockStation();

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
            put('!', List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_1));
            put(' ', List.of(KeyEvent.VK_SPACE));
            put('+', List.of(KeyEvent.VK_PLUS));
            put('-', List.of(KeyEvent.VK_MINUS));
            put('_', List.of(KeyEvent.VK_UNDERSCORE));
            put('.', List.of(KeyEvent.VK_PERIOD));
            put(';', getSemicolonEvent());
            put(':', getColonEvent());
        }};
    }

    private static List<Integer> getColonEvent() {
        //defaults for en_us
        return List.of(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON);
    }

    private static List<Integer> getSemicolonEvent() {
        //defaults for en_us
        return List.of(KeyEvent.VK_SEMICOLON);
    }

    public abstract void botAction();

    @Override
    public void run() {
        try {
            botAction();
            terminateBot();
        } catch (UnexpectedSystemManipulationException uem){
            lockStation();
        } finally {
            if(dispose()) {
                log.info("robot disposed");
            }
        }
    }

    protected abstract boolean dispose();

    protected void terminateBot() {

        switch (onFinishAction) {
            case NOTHING:
                log.info("Bot completed");
                return;
            case HIBERNATE:
                log.info("Bot completed, system hibernate");
                sysHibernate();
                return;
            case SLEEP:
                log.info("Bot completed, system sleep");
                sysSleep();
                return;
            case SHUTDOWN:
                log.info("Bot completed, system shutdown");
                sysShutDown();
                return;
            case LOCK:
                log.info("Bot completed, locking system");
                lockStation();
                return;
            default:
                throw new UnsupportedOperationException("Operation not yet supported: " + onFinishAction);
        }
    }

    protected abstract void sysShutDown();

    protected abstract void sysSleep();

    protected abstract void sysHibernate();

    protected abstract void fileNew();

    protected abstract void closeProgram(boolean sleep) throws InterruptedException;

    protected abstract void deleteAll(boolean sleep) throws InterruptedException;

    protected abstract void newWindow() throws InterruptedException;
}
