package com.acabra.robot.bot;

import com.acabra.robot.security.SecuritySettings;
import lombok.extern.slf4j.Slf4j;

import java.awt.Toolkit;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class BotFactory {
    private BotFactory() {}

    public static void startBot(String... args) {
        try {
            BotConfig config = BotConfig.fromArgs(args);
            OsType type = OsType.getOsType();
            SecuritySettings secSettings = SecuritySettings.of(config.lockOnChange, config.panicMode);
            switch (type) {
                case MAC:
                    MacBot mac = new MacBot(config.loopingText, config.executionType, config.onFinishAction,
                            secSettings, config.executionMap);
                    execute(mac, config.runningTime, config.timeUnit);
                    break;
                case WIN:
                    WinBot win = new WinBot(config.loopingText, config.executionType, config.onFinishAction,
                            secSettings, config.executionMap);
                    execute(win, config.runningTime, config.timeUnit);
                    break;
                default:
                    throw new UnsupportedOperationException("No implementation bot found for OS: " + OsType.OS_NAME);
            }
        } catch (Exception e) {
            log.error("error while launching the bot: {}", e.getMessage(), e);
        }
    }

    private static void execute(ImprovedBot bot, long secondsRunning, TimeUnit timeUnit) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(bot, executor);
        Executor dEx = CompletableFuture.delayedExecutor(secondsRunning, timeUnit);
        CompletableFuture.runAsync(()-> requestShutdown(bot, executor), dEx);
    }

    private static void requestShutdown(ImprovedBot bot, ExecutorService executor) {
        bot.requestShutdown();
        List<Runnable> tasks = executor.shutdownNow();
        if(tasks.size() > 0) {
            log.info("Success termination unfinished tasks" + tasks.size());
        }
    }

}
