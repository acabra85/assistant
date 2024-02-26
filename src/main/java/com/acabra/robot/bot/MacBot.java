package com.acabra.robot.bot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import com.acabra.robot.security.SecuritySettings;
import com.acabra.robot.spotbugs.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MacBot extends ImprovedBot {
    @SuppressFBWarnings(value="CT_CONSTRUCTOR_THROW", justification="it's ok")
    public MacBot(String loopText, ExecutionType executionType, OnFinishAction onFinishAction,
                  SecuritySettings secSettings, Map<String, String> executionVariables) throws AWTException {
        super(loopText, executionType, onFinishAction, secSettings, executionVariables);
        log.info("Running the MAC os bot ...");
    }

    @Override
    protected void lockStation() {
        pressCombined(KeyEvent.VK_META, KeyEvent.VK_CONTROL, KeyEvent.VK_Q);
    }

    @Override
    protected void openRunWindow() throws InterruptedException {
        pressCommandWith(KeyEvent.VK_SPACE);
    }

    private void pressCommandWith(int evt) {
        pressCombined(KeyEvent.VK_META, evt);
    }

    @Override
    protected void minimizeAll() throws InterruptedException {
        pressCommandWith(KeyEvent.VK_M);
    }

    @Override
    protected void fileNew() {
        pressCommandWith(KeyEvent.VK_N);
    }

    @Override
    protected void closeProgram(boolean sleep) throws InterruptedException {
        pressCommandWith(KeyEvent.VK_Q);
        if(sleep) Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    protected void deleteAll(boolean sleep) throws InterruptedException {
        pressCommandWith(KeyEvent.VK_A);
        keyStroke(KeyEvent.VK_BACK_SPACE);
        if (sleep) Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    protected boolean dispose(Robot parent) {
        String className = "java.awt.Robot";
        String privateFieldName = "peer";
        String methodName = "dispose";
        Field f = null;
        boolean modifiedAccessor = false;
        try {
            f = Class.forName(className).getDeclaredField(privateFieldName);
            if(!f.canAccess(parent)) {
                f.setAccessible(true);
                modifiedAccessor = true;
            }
            Object o = f.get(parent);
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

    @Override
    protected void sysShutDown() {

    }

    @Override
    protected void sysSleep() {

    }

    @Override
    protected void sysHibernate() {

    }

    @Override
    protected void newWindow() throws InterruptedException {
        pressCommandWith(CHAR_EVT_MAP.get('n').get(0));
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

    @Override
    protected String getNotepadName() {
        return "TextEdit";
    }

    private void closeVim() throws InterruptedException {
        vimDeleteAll();
        List<Integer> bang = CHAR_EVT_MAP.get('!');
        Integer q = CHAR_EVT_MAP.get('q').get(0);
        List<Integer> colonEvt = CHAR_EVT_MAP.get(':');
        pressCombined(colonEvt);
        keyStroke(q);
        pressCombined(bang);
        closeProgram(true);
    }

    private void vimDeleteAll() throws InterruptedException {
        pressEsc();
        List<Integer> g = CHAR_EVT_MAP.get('g');
        List<Integer> d = CHAR_EVT_MAP.get('d');
        List<Integer> G = CHAR_EVT_MAP.get('G');
        keyStroke(g);
        keyStroke(g);
        keyStroke(d);
        keyStroke(G);
        Thread.sleep(DEFAULT_ACTION_DELAY);
    }

}
