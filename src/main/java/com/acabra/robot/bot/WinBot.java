package com.acabra.robot.bot;

import com.acabra.robot.exception.UnexpectedSystemManipulationException;
import com.acabra.robot.security.SecuritySettings;
import lombok.extern.slf4j.Slf4j;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class WinBot extends ImprovedBot {

    public WinBot(String loopText, ExecutionType executionType, OnFinishAction onFinishAction, SecuritySettings secSettings,
                  Map<String, String> executionVariables) throws AWTException {
        super(loopText, executionType, onFinishAction, secSettings, executionVariables);
        log.info("Started WinBot ...");
    }

    //static { System.loadLibrary("lib/com_acabra_robot_ImprovedBot"); }

    @Override
    protected void unlockStation() {
        pressCtrlAltDel();
    }

    @Override
    protected void lockStation() {
        try {
            Runtime.getRuntime().exec("rundll32 user32.dll,LockWorkStation");
        } catch (Exception e) {
            log.error("Failed to lock: " + e.getMessage());
        }
    }

    @Override
    protected void openRunWindow() throws InterruptedException {
        pressCombined(KeyEvent.VK_WINDOWS, KeyEvent.VK_R);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    protected void minimizeAll() throws InterruptedException {
        keyPress(KeyEvent.VK_WINDOWS);
        keyStroke(CHAR_EVT_MAP.get('m'));
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
            if(t instanceof UnexpectedSystemManipulationException) {
                throw (UnexpectedSystemManipulationException)t;
            }
        }
        log.info("Program finished");
    }

    @Override
    protected boolean dispose() {
        String className = "java.awt.Robot";
        String privateFieldName = "peer";
        String methodName = "disposeImpl";
        Field f = null;
        boolean modifiedAccessor = false;
        try {
            f = Class.forName(className).getDeclaredField(privateFieldName);
            if(!f.canAccess(this)) {
                f.setAccessible(true);
                modifiedAccessor = true;
            }
            Object o = f.get(this);
            Method method = o.getClass().getDeclaredMethod(methodName);

            boolean modifiedMethodAccessor = false;
            try {
                if(!method.canAccess(o)) {
                    method.setAccessible(true);
                    modifiedMethodAccessor = true;
                }
                method.invoke(o);
                return true;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if(modifiedMethodAccessor) {
                    method.setAccessible(false);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if(modifiedAccessor) {
                f.setAccessible(false);
            }
        }
        return false;
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
        try {
            minimizeAll();
            closeProgram(true);
            keyStroke(KeyEvent.VK_TAB);
            keyStroke(KeyEvent.VK_SPACE);
        } catch (Exception e) {
            log.error("Failed to hibernate: " + e.getMessage());
        }
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

    private void pressCtrlAltDel() {
        pressCombined(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_DELETE);
    }

    //private native String getKeyboardLanguage();
}
