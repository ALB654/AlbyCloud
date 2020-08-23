package de.alb654.AlbyCloud.screens;

import de.alb654.AlbyCloud.Main;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public class ScreenReader {

    private ScheduledExecutorService scheduledExecutorService;

    public void start() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {

            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();

            String[] contents = line.split(" ");
            String cmd = contents[0];
            String[] args = new String[contents.length - 1];

            for(int i = 1; i < contents.length; i++) {
                args[i - 1] = contents[i];
            }
            try {
                Main.getAlbyCloud().getScreenManager().getActiveScreen().executeCommand(cmd, args, line);
            } catch (Exception e){
                Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).addError(e, "Ausführen vom Command '" + cmd + "'");
            }


        }, 1000, 200, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduledExecutorService.shutdownNow();
    }

}
