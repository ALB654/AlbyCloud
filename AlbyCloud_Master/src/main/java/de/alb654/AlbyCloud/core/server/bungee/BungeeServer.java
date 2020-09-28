package de.alb654.AlbyCloud.core.server.bungee;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.commandMangers.BungeeCommandManager;
import de.alb654.AlbyCloud.core.server.ExternalProcess;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.file.FileManager;
import de.alb654.AlbyCloud.screens.LogLevel;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.utils.Waiter;
import de.alb654.network.bungee.BungeeOutShutdownPacket;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 19.6.2020
 */
public class BungeeServer extends ExternalProcess {

    private final int ram;
    private final FileManager fileManager;
    private final ConfigManager configManager;

    private final File bungeeFolder;
    private final Waiter waiter;

    private boolean running;
    private boolean timeouted;

    private Screen cloudScreen;

    public BungeeServer(int ram, FileManager fileManager, ConfigManager configManager) {
        this.ram = ram;
        this.fileManager = fileManager;
        this.bungeeFolder = fileManager.getFolder(FileManager.Folder.BUNGEE);
        this.configManager = configManager;

        this.waiter = new Waiter();
        this.running = false;
        this.timeouted = false;
    }

    public void setCloudScreen(Screen screen) {
        cloudScreen = screen;
    }

    public void start() {
        setup();

        startProcess("");
        startBungeeStartTimeout();
    }

    public synchronized void stop() {
        Main.getAlbyCloud().getCloudNetworkServer().sendPacket("BUNGEE", new BungeeOutShutdownPacket());
        cloudScreen.addMsg("Stoppe Bungeecord...");
        startBungeeStopTimeout();

        waiter.waitFor("Bungeecord stop");
    }

    private void setup() {
        setProcessFileName("bungee.jar");
        setProcessFolder(bungeeFolder);
        setRam(ram);
    }

    @Override
    protected void postStart() {
        Screen screen = Main.getAlbyCloud().getScreenManager().addScreen("Bungee", BungeeCommandManager.class);
        BufferedReader br = new BufferedReader(new InputStreamReader(getProcess().getInputStream()));

        try {

            String ln;
            while ((ln = br.readLine()) != null) {
                if (!ln.equals(">"))
                    screen.addMsg(ln);
            }

        } catch (IOException e) {
            cloudScreen.addError(e, "schreiben in Screen von 'Bungee'");
        }
    }

    @Override
    protected boolean preStart() {
        if(!checkIfNeededFilesExists()) {
            File plugins = new File(bungeeFolder, "plugins");
            plugins.mkdir();
            return loadDefaultFile("BungeePlugin.jar", plugins) &&
                    loadDefaultFile("bungee.jar", bungeeFolder) &&
                    loadDefaultFile("config.yml", bungeeFolder);

        }
        return true;
    }

    @Override
    protected void onStartFailure() {}

    @Override
    protected void onStop() { }

    @Override
    protected void postStop() {
        if(running) {
            running = false;
            Main.getAlbyCloud().getScreenManager().deleteScreen("Bungee");
            Main.getAlbyCloud().getCloudNetworkServer().removeClient("Bungee");
            cloudScreen.addMsg("Bungeecord wurde gestoppt!");

            waiter.call();
        }
    }

    private void startBungeeStopTimeout() {
        new Thread(() -> {
            try {
                Thread.sleep(Long.parseLong(configManager.getValue(ConfigManager.BUNGEE_START_TIMEOUT_DELAY)));
            } catch (InterruptedException e) {
                cloudScreen.addError(e, "warten auf den Bungeestop Timeout");
            }
            if(running) {
                timeouted = true;
                stopProcess();
                waiter.call();
                cloudScreen.addMsg("Bungeecord ist nicht gestoppt! Stoppe Process...", LogLevel.ERROR);
            }
        }).start();
    }

    private void startBungeeStartTimeout() {
        new Thread(() -> {
            try {
                Thread.sleep(Long.parseLong(configManager.getValue(ConfigManager.BUNGEE_START_TIMEOUT_DELAY)));
            } catch (InterruptedException e) {
                cloudScreen.addError(e, "warten auf den Bungeestart Timeout");
            }
            if(!running) {
                timeouted = true;
                stopProcess();
                waiter.call();
                cloudScreen.addMsg("Konnte Bungeecord nicht starten! Er hat nicht geantwortet", LogLevel.ERROR);
            }
        }).start();
    }

    public void confirmStart() {
        if(timeouted) {
            cloudScreen.addMsg("BungeeServer hat Start nach Timeout bestätigt!", LogLevel.WARNING);
            return;
        }
        this.running = true;
        cloudScreen.addMsg("Bungeecord gestartet!");
        waiter.call();
    }

    private boolean loadDefaultFile(String fileName, File file) {
        try {
            fileManager.loadRessource(file, fileName);
        } catch (IOException e) {
            cloudScreen.addError(e, "laden der default " + fileName);
            cloudScreen.addMsg("Konnte '" + fileName + "' nicht laden!", LogLevel.ERROR);
            return false;
        }
        return true;
    }

    private boolean checkIfNeededFilesExists() {
        boolean containsJarAndConfig = bungeeFolderContainsFile("bungee.jar") && bungeeFolderContainsFile("config.yml");
        boolean containsPlugin = false;
        File plugins = new File(bungeeFolder, "plugins");
        if(plugins.exists() && plugins.isDirectory())
            if(new File(plugins, "BungeePlugin.jar").exists())
                containsPlugin = true;

        return containsJarAndConfig && containsPlugin;
    }

    private boolean bungeeFolderContainsFile(String name) {
        for(File f : Objects.requireNonNull(bungeeFolder.listFiles())) {
            if(f.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public boolean isRunning() {
        return running;
    }
}
