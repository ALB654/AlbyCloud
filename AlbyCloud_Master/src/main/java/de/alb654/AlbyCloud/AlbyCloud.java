package de.alb654.AlbyCloud;

import de.alb654.AlbyCloud.commands.commandMangers.CloudCommandManager;
import de.alb654.AlbyCloud.core.RamManager;
import de.alb654.AlbyCloud.core.groups.GroupManager;
import de.alb654.AlbyCloud.core.network.CloudNetworkServer;
import de.alb654.AlbyCloud.core.server.ServerManager;
import de.alb654.AlbyCloud.core.server.bungee.BungeeServer;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.screens.ScreenManager;
import de.alb654.AlbyCloud.screens.ScreenReader;
import de.alb654.AlbyCloud.utils.ConsoleColor;
import de.alb654.AlbyCloud.utils.Utils;
import de.alb654.AlbyCloud.utils.Waiter;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 14.10.2019.
 */
public class AlbyCloud {

    private long startingTime;
    private final ScreenManager screenManager;
    private final ScreenReader screenReader;
    private final ConfigManager configManager;
    private final ServerManager serverManager;
    private final RamManager ramManager;
    private final BungeeServer bungeeServer;
    private final CloudNetworkServer cloudNetworkServer;
    private GroupManager groupManager;

    private boolean shutdown;

    public AlbyCloud(ScreenManager screenManager, ScreenReader screenReader, ConfigManager configManager, ServerManager serverManager, RamManager ramManager, BungeeServer bungeeServer, CloudNetworkServer cloudNetworkServer) {
        this.screenManager = screenManager;
        this.screenReader = screenReader;
        this.configManager = configManager;
        this.serverManager = serverManager;
        this.ramManager = ramManager;
        this.bungeeServer = bungeeServer;
        this.cloudNetworkServer = cloudNetworkServer;

        this.shutdown = false;
    }

    public synchronized void start() {
        screenManager.addScreen(ScreenManager.CLOUD_SCREEN, CloudCommandManager.class);
        screenManager.activateScreen(ScreenManager.CLOUD_SCREEN);
        Screen cloud = screenManager.getScreen(ScreenManager.CLOUD_SCREEN);

        this.groupManager = new GroupManager(cloud);

        startingTime = System.currentTimeMillis();
        cloud.addMsg("Starte Cloud...");
        Utils.printStartBanner();

        cloud.addMsg("Starte Command-System...");
        screenReader.start();

        cloud.addMsg("Starte Netzwerk-Server...");
        cloudNetworkServer.setCloudScreen(cloud);
        cloudNetworkServer.connect();
        cloudNetworkServer.startListneningToClients();

        cloud.addMsg("Starte Bungeecord...");
        bungeeServer.setCloudScreen(cloud);
        bungeeServer.start();
        bungeeServer.getWaiter().waitFor("Bungeecord start");
        if(!bungeeServer.isRunning())
            return;

        cloud.addMsg("Cloud gestartet!");
    }

    public synchronized void stop() {
        shutdown = true;

        Screen cloud = screenManager.getScreen(ScreenManager.CLOUD_SCREEN);
        cloud.addMsg("Stoppe Command-System...");
        screenReader.stop();

        groupManager.deaktivateAllGroups();
        serverManager.stopAllServer();

        bungeeServer.stop();

        cloud.addMsg("Stoppe Netzwerk-Server...");
        cloudNetworkServer.stop();

        cloud.addMsg(ConsoleColor.ANSI_CYAN.Id() + "Cloud gestoppt! " + ConsoleColor.ANSI_RESET.Id() + "Onlinezeit: " + Utils.toTimeString(System.currentTimeMillis() - startingTime));
        System.exit(0);
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public RamManager getRamManager() {
        return ramManager;
    }

    public BungeeServer getBungeeServer() {
        return bungeeServer;
    }

    public CloudNetworkServer getCloudNetworkServer() {
        return cloudNetworkServer;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
