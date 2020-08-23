package de.alb654.AlbyCloud.screens;

import de.alb654.AlbyCloud.commands.CommandManager;
import de.alb654.AlbyCloud.commands.commandMangers.CloudCommandManager;
import de.alb654.AlbyCloud.commands.commandMangers.CommandManagerCollection;
import de.alb654.AlbyCloud.file.FileManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 14.10.2019.
 */
public class ScreenManager {

    public static final String ERROR_SCREEN = "ErrorScreen";
    public static final String CLOUD_SCREEN = "Cloud";

    private ArrayList<Screen> screens;
    private Screen activeScreen;

    private FileManager fileManager;
    private CommandManagerCollection commandManagerCollection;

    private LogAmount logAmount;

    public ScreenManager(FileManager fileManager, CommandManagerCollection commandManagerCollection, LogAmount logAmount) {
        this.commandManagerCollection = commandManagerCollection;
        this.screens = new ArrayList<>();
        this.fileManager = fileManager;
        this.logAmount = logAmount;

        addErrorScreen(fileManager);

        activeScreen = getScreen(ScreenManager.ERROR_SCREEN);
    }

    private void addErrorScreen(FileManager fileManager) {
        screens.add(new Screen(ERROR_SCREEN, this, commandManagerCollection.getCommandManager(CloudCommandManager.class), fileManager));
        getScreen(ERROR_SCREEN).addMsg("===================================", LogLevel.ERROR);
        getScreen(ERROR_SCREEN).addMsg("Das ist der Error Screen. ", LogLevel.ERROR);
        getScreen(ERROR_SCREEN).addMsg("Hier werden Fehler angezeigt. ", LogLevel.ERROR);
        getScreen(ERROR_SCREEN).addMsg("===================================", LogLevel.ERROR);
    }

    public Screen getScreen(String name) {
        for(Screen s : screens) if (s.getName().equalsIgnoreCase(name)) return s;
        return getScreen(ERROR_SCREEN);
    }

    public Screen addScreen(String name, Class<? extends CommandManager> commandManager) {
        if(getScreen(name).getName().equals(ERROR_SCREEN) && !name.equals(ERROR_SCREEN))
            screens.add(new Screen(name, this, commandManagerCollection.getCommandManager(commandManager), fileManager));
        return getScreen(name);
    }

    public boolean deleteScreen(String name) {
        if(!name.equals(ERROR_SCREEN))
            return screens.remove(getScreen(name));
        return false;
    }

    public void activateScreen(String name) {
        activeScreen.deactivate();
        activeScreen = getScreen(name).activate();
    }

    public Screen getActiveScreen() {
        return activeScreen;
    }

    public LogAmount getLogAmount() {
        return logAmount;
    }

    public enum LogAmount {
        ALL, NONE, ERROR;

        public static LogAmount fromString(String amount) {
            for(LogAmount la : values())
                if(la.name().equalsIgnoreCase(amount))
                    return la;
            return null;
        }
    }
}
