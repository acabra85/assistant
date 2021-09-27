package com.acabra.robot.bot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MacBot extends ImprovedBot {

    public MacBot(String loopText, ExecutionType executionType, OnFinishAction onFinishAction) throws AWTException {
        super(OsType.MAC, loopText, executionType, onFinishAction);
        log.info("Running the MAC os bot ...");
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

    private void notepadTypeAction() throws InterruptedException {
        try {
            runCommand("TextEdit");
            fileNew();
            noFormatText();
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

    private void noFormatText() {
        keyPress(KeyEvent.VK_META);
        pressCombined(CHAR_EVT_MAP.get('T'));
        keyRelease(KeyEvent.VK_META);
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
