package com.acabra.robot.bot;

import lombok.extern.slf4j.Slf4j;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;

@Slf4j
public class WinBot extends ImprovedBot {

    public WinBot(String loopText, ExecutionType executionType, OnFinishAction onFinishAction) throws AWTException {
        super(OsType.WIN, loopText, executionType, onFinishAction);
        log.info("Started WinBot ...");
    }

    //static { System.loadLibrary("lib/com_acabra_robot_ImprovedBot"); }

    @Override
    protected void openRunWindow() throws InterruptedException {
        pressCombined(KeyEvent.VK_WINDOWS, KeyEvent.VK_R);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    protected void minimizeAll() throws InterruptedException {
        keyPress(KeyEvent.VK_WINDOWS);
        type('m');
        keyRelease(KeyEvent.VK_WINDOWS);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    public void botAction() {
        try {
            switch (this.executionType) {
                case NOTEPAD_TYPE:
                    notepadTypeAction();
                    break;
                case MOUSE_MOVER:
                    mouseMoverAction();
                    break;
                default:
                    throw new UnsupportedOperationException("Unimplemented type: " + this.executionType);

            }
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
        log.info("Program finished");
    }

    @SuppressWarnings("BusyWait")
    private void mouseMoverAction() throws InterruptedException {
        boolean cycle = false;
        while(continueRunning()) {
            Point curLocation = MouseInfo.getPointerInfo().getLocation();
            mouseMove(curLocation.x, curLocation.y + (cycle ? 10 : -10) );
            cycle = !cycle;
            Thread.sleep(10000L);
        }
    }

    @SuppressWarnings("BusyWait")
    private void notepadTypeAction() {
        try {
            runCommand("notepad");
            fileNew();
            while (continueRunning()) {
                typeText(loopText);
                deleteAll(true);
                Thread.sleep(LOOP_SLEEP);
            }
            deleteAll(true);
            closeProgram(true);
        } catch (InterruptedException ie) {
            log.error(ie.getMessage());
            try {
                deleteAll(false);
                closeProgram(false);
            } catch (Exception e) {
                log.error("Unable to gracefully close the system: {}", e.getMessage(), e);
            }
        }
    }

    @Override
    protected void sysShutDown() {

    }

    @Override
    protected void sysSleep() {

    }

    @Override
    protected void sysHibernate() {
        try {
            minimizeAll();
            closeProgram(true);
            keyStroke(CHAR_EVT_MAP.get('h'));
            keyStroke(KeyEvent.VK_TAB);
            keyStroke(KeyEvent.VK_SPACE);
        } catch (Exception e) {
            log.error("Failed to hibernate: " + e.getMessage());
        }
    }

    @Override
    protected void fileNew() {
        pressCtrlWith(KeyEvent.VK_N);
    }

    @Override
    protected void closeProgram(boolean sleep) throws InterruptedException {
        pressCombined(KeyEvent.VK_ALT, KeyEvent.VK_F4);
        if(sleep) Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    protected void deleteAll(boolean sleep) throws InterruptedException {
        pressCombined(KeyEvent.VK_CONTROL, KeyEvent.VK_A);
        keyStroke(KeyEvent.VK_BACK_SPACE);
        if (sleep) Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    protected void newWindow() throws InterruptedException{
        pressCtrlWith(CHAR_EVT_MAP.get('n').get(0));
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    //private native String getKeyboardLanguage();
}
