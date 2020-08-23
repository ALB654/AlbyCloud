package de.alb654.AlbyCloud;

import de.alb654.AlbyCloud.commands.commandMangers.CommandManagerCollection;
import de.alb654.AlbyCloud.core.RamManager;
import de.alb654.AlbyCloud.core.network.CloudNetworkServer;
import de.alb654.AlbyCloud.core.server.ServerManager;
import de.alb654.AlbyCloud.core.server.bungee.BungeeServer;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.file.FileManager;
import de.alb654.AlbyCloud.screens.ScreenManager;
import de.alb654.AlbyCloud.screens.ScreenReader;
import de.alb654.AlbyCloud.utils.ConsoleColor;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 14.10.2019.
 */
public class Main {

    private static AlbyCloud albyCloud;

    public static void main(String[] args) {
        FileManager fileManager = new FileManager(args.length > 0 ? args[0] : "./AlbyCloud");
        ConfigManager configManager = new ConfigManager(fileManager, "Config.json");

        CommandManagerCollection collection = new CommandManagerCollection();
        ScreenManager screenManager = new ScreenManager(fileManager, collection, ScreenManager.LogAmount.valueOf(configManager.getValue(ConfigManager.LOG_AMOUNT).toUpperCase()));

        RamManager ramManager = new RamManager(Integer.parseInt(configManager.getValue(ConfigManager.RAM)));
        ServerManager serverManager = new ServerManager(configManager, fileManager, ramManager);
        BungeeServer bungeeServer = new BungeeServer(Integer.parseInt(configManager.getValue(ConfigManager.BUNGEE_RAM)), fileManager, configManager);

        CloudNetworkServer cloudNetworkServer = new CloudNetworkServer(9977);

        try {
            albyCloud = new AlbyCloud(screenManager, new ScreenReader(), configManager, serverManager, ramManager, bungeeServer, cloudNetworkServer);
            albyCloud.start();
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColor.ANSI_RED + "Fehler beim starten der Cloud! Sind die Daten in der Config richtig?");
        }
    }

    public static AlbyCloud getAlbyCloud() {
        return albyCloud;
    }
}
