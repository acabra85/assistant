package com.acabra.robot.security;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.function.BiFunction;

@Slf4j
public class SecurityMonitor {
    final int x;
    final int y;
    final Color pixelColor;
    private final boolean lockOnChange;
    private Dimension screenSize;
    private final BiFunction<Integer, Integer, Color> getColor;
    private final boolean panicMode;

    public SecurityMonitor(Dimension screenSize, SecuritySettings secSettings, PointerInfo pointerInfo,
                           BiFunction<Integer, Integer, Color> getColor) {
        Point location = pointerInfo.getLocation();
        this.x = location.x;
        this.y = location.y;
        this.getColor = getColor;
        this.pixelColor = getColor.apply(location.x, location.y);
        this.lockOnChange = secSettings.lockOnChange;
        this.panicMode = secSettings.panicMode;
        this.screenSize = (Dimension) screenSize.clone();
    }

    public boolean shouldLockStation(Point curLocation) {
        if(!lockOnChange) {
            return false;
        }
        Dimension curScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if(panicMode || activateMonitorWhenScreenGetsBigger(curScreenSize)) {
            if(this.x != curLocation.x || Math.abs(this.y - curLocation.y) > 1) {
                log.info("Previous location: [{},{}] new Location {}", this.x, this.y, curLocation);
                return true;
            }
            Color curColor = getColor.apply(curLocation.x, curLocation.y);
            if(!this.pixelColor.equals(curColor)) {
                log.info("Previous color: {} new Color {}", this.pixelColor, curColor);
                return true;
            }
        }
        return false;
    }

    private boolean activateMonitorWhenScreenGetsBigger(Dimension curScreenSize) {
        //lock only if the screen size got bigger
        boolean screenGotBigger = screenSize.height < curScreenSize.height || screenSize.width < curScreenSize.width;
        if(screenGotBigger) {
            log.info("Prev Size: {} , new Size: {}", screenSize, curScreenSize);
        }
        screenSize = curScreenSize;
        return screenGotBigger;
    }
}