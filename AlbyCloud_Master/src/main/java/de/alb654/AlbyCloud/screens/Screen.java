package de.alb654.AlbyCloud.screens;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.CommandManager;
import de.alb654.AlbyCloud.file.FileManager;
import de.alb654.AlbyCloud.utils.ConsoleColor;
import de.alb654.AlbyCloud.utils.LimitedArray;
import de.alb654.AlbyCloud.utils.Utils;
import de.alb654.network.bungee.BungeeOutCloudMessagePacket;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 14.10.2019.
 */
public class Screen {

    private static final boolean DEBUG_MODE = true;

    private final LimitedArray<String> msgs = new LimitedArray<>(150);
    private String name;
    private boolean active;
    private File logFile;

    private ScreenManager manager;
    private CommandManager commandManager;

    public Screen(String name, ScreenManager manager, CommandManager commandManager, FileManager fileManager) {
        this.name = name;
        this.manager = manager;
        this.commandManager = commandManager;
        this.active = false;

        setup(fileManager);
    }

    private void setup(FileManager fileManager) {
        if(manager.getLogAmount() == ScreenManager.LogAmount.ALL || (manager.getLogAmount() == ScreenManager.LogAmount.ERROR && name.equals(ScreenManager.ERROR_SCREEN))) {
            File logFolder = fileManager.createFolderInFolder(FileManager.Folder.LOG, WordUtils.capitalize(name.toLowerCase()));

            int i = 0;
            String filename;
            do {
                filename = name + "#" + i + ".log";
                i++;
            } while (fileManager.containsFolderFile(logFolder, filename));

            logFile = fileManager.createFileInFolder(logFolder, filename);
        }
    }

    public void executeCommand(String name, String[] args, String label) {
        addMsg(label, LogLevel.INPUT);
        commandManager.executeCommand(name, args, (success, msg) -> {
            if(success)
                addMsg("Command '" + name + "' ausgeführt! > " + msg);
            else
                addMsg("Konnte Command '" + name + "' nicht ausführen > " + msg, LogLevel.WARNING);
        });
    }

    public void addError(Exception e, String where) {
        Screen errSc = manager.getScreen(ScreenManager.ERROR_SCREEN);
        addMsg("Fehler beim '" + where + "'", LogLevel.ERROR);
        addMsg("Das sollte nicht passieren!", LogLevel.ERROR);

        errSc.addMsg(" - ");
        errSc.addMsg("Fehler beim '" + where + "' (Screen: '" + name + "')", LogLevel.ERROR);
        errSc.addMsg("Caused by: " + e.toString());

        for(StackTraceElement element : e.getStackTrace()) {
            errSc.addMsg(element.toString(), LogLevel.ERROR);
        }
    }

    public void addMsg(String msg, LogLevel level) {
        if(level == LogLevel.DEBUG && !DEBUG_MODE)
            return;

        String time = new SimpleDateFormat().format(new Date(System.currentTimeMillis()));
        String fmsg = level.getColor().Id() + time + " | " + name + " | " + level.getPrefix() + msg;
        msgs.add(fmsg);

        if(manager.getLogAmount() == ScreenManager.LogAmount.ALL || (manager.getLogAmount() == ScreenManager.LogAmount.ERROR && level == LogLevel.ERROR))
            Utils.writeToFile(logFile, fmsg + "\n", false);

        if(this.name.equals(ScreenManager.CLOUD_SCREEN) && (level == LogLevel.INFO || level == LogLevel.WARNING || level == LogLevel.ERROR) && !Main.getAlbyCloud().isShutdown()) {
            Main.getAlbyCloud().getCloudNetworkServer().sendPacket("Bungee", new BungeeOutCloudMessagePacket(msg + " (" + level.name() + ")"));
        }

        if(active)
            System.out.println(fmsg);
    }

    public void addMsg(String msg) {
        addMsg(msg, LogLevel.INFO);
    }

    public Screen activate() {
        active = true;
        for(int i = 0; i < msgs.size(); i++) {
            System.out.println(msgs.get(i));
        }
        return this;
    }

    public void deactivate() {
        active = false;
        System.out.println("  ");
        System.out.println(ConsoleColor.ANSI_BLUE.Id() + "====================" + ConsoleColor.ANSI_RESET.Id());
        System.out.println("  ");
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
