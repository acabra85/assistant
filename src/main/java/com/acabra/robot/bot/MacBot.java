package com.acabra.robot.bot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MacBot extends ImprovedBot {

    public MacBot(String loopText, OnFinishAction onFinishAction) throws AWTException {
        super(OsType.MAC, loopText, onFinishAction);
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

    }

    @Override
    public void botAction() {
        try {
            runCommand("terminal.app");
            newWindow();
            /*execute("vi .deleteme");
            while (continueRunning()) {
                pressRelease(CHAR_EVT_MAP.get('i').get(0));
                typeText(loopText);
                vimDeleteAll();
                Thread.sleep(LOOP_SLEEP);
            }
            closeVim();*/
        } catch (InterruptedException ie) {
            log.error(ie.getMessage(), ie);
            try {
                closeVim();
            } catch (Exception e) {
                log.error("Unable to gracefully close the system: {}", e.getMessage(), e);
            }
            Thread.currentThread().interrupt();
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
