package com.acabra.robot.bot;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BotFactory {
    private BotFactory() {}

    public static void startBot(String... args) {
        try {
            BotConfig config = BotConfig.fromArgs(args);
            OsType type = OsType.getOsType();
            switch (type) {
                case MAC:
                    execute(new MacBot(config.loopingText), config.runningTime, config.timeUnit);
                    break;
                case WIN:
                    execute(new WinBot(config.loopingText), config.runningTime, config.timeUnit);
                    break;
                default:
                    throw new UnsupportedOperationException("No implementation bot found for OS: " + OsType.OS_NAME);
            }
        } catch (Exception e) {
            log.error("error while launching the bot: {}", e.getMessage(), e);
        }
    }

    private static void execute(ImprovedBot bot, long secondsRunning, TimeUnit timeUnit) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture.runAsync(() -> {
            bot.run();
//            int i = JOptionPane.showConfirmDialog(null, "Shutdown?");
//            if (i == JOptionPane.OK_OPTION) {
//                requestShutdown(bot, executor);
//            }
        }, executor);
        CompletableFuture.runAsync(()-> requestShutdown(bot, executor),
                CompletableFuture.delayedExecutor(secondsRunning, timeUnit));
    }

    private static void requestShutdown(ImprovedBot bot, ExecutorService executor) {
        bot.requestShutdown();
        List<Runnable> tasks = executor.shutdownNow();
        if(tasks.size() > 0) {
            log.info("Success termination unfinished tasks" + tasks.size());
        }
    }


}
