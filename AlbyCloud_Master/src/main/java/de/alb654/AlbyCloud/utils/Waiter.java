package de.alb654.AlbyCloud.utils;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 19.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.screens.ScreenManager;

public class Waiter {

    public synchronized byte waitFor(String waitfor) {
        try {
            wait();
        } catch (InterruptedException e) {
            Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).addError(e, "warten auf '" + waitfor + "'");
            return -1;
        }
        return 0;
    }

    public synchronized void call() {
        notify();
    }

}
