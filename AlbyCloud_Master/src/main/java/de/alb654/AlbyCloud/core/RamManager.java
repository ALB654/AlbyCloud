package de.alb654.AlbyCloud.core;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.screens.LogLevel;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.screens.ScreenManager;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 19.06.2020.
 */
public class RamManager {

    private final int cloudRam;
    private int usedRam;

    public RamManager(int cloudRam) {
        this.cloudRam = cloudRam;
    }

    public boolean reserveRam(int ram) {
        Screen cloudScreen = Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN);

        if(cloudRam - usedRam >= ram) {
            usedRam += ram;
            cloudScreen.addMsg(ram + "mb Ram beansprucht (" + usedRam + "/" + cloudRam + ", " + getPercentUsed() + "%)", LogLevel.DEBUG);
            return true;
        }
        cloudScreen.addMsg(ram + "mb Ram konnte nicht beansprucht werden", LogLevel.DEBUG);
        return false;
    }

    public void freeRam(int ram) {
        Screen cloudScreen = Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN);

        if(usedRam - ram < 0) {
            cloudScreen.addMsg("Es wurde mehr Ram freigegeben (" + ram + "mb) als in benutzung (" + usedRam + "mb) ist ", LogLevel.WARNING);
            usedRam = 0;
        } else {
            usedRam -= ram;
            cloudScreen.addMsg("Es wurde " + ram + "mb Ram freigegeben (" + usedRam + "/" + cloudRam + ", " + getPercentUsed() + "%)", LogLevel.DEBUG);
        }
    }

    private int getPercentUsed() {
        return (int)(((double)usedRam/(double)cloudRam)*100);
    }

}
