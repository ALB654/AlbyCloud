package de.alb654.AlbyBungeePlugin;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 20.6.2020
 */

import de.alb654.AlbyBungeePlugin.commands.BanMsgCommand;
import de.alb654.AlbyBungeePlugin.commands.CloudMsgCommand;
import de.alb654.AlbyBungeePlugin.events.JoinEvent;
import de.alb654.AlbyBungeePlugin.events.OnChatEvent;
import de.alb654.AlbyBungeePlugin.mongo.MongoManager;
import de.alb654.AlbyBungeePlugin.network.MasterConnection;
import de.alb654.AlbyBungeePlugin.network.SpigotConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {

    public static final String Cprefix = "[BloodCloud] ";
    public static final String Bprefix = "§b§lBloodMC §8• §7";
    public static final String BprefixWithoutDot = "§b§lBloodMC §7";
    public static final String DOT = "§8•§7";
    public static final String BCprefix = "§b§lBloodCloud §8• §7";


    private static final int MASTER_PORT = 9977;

    private static BungeePlugin plugin;

    private MasterConnection masterConnection;
    private MongoManager mongoManager;

    private CloudMsgCommand cloudMsgCommand;
    private BanMsgCommand banMsgCommand;

    @Override
    public void onEnable() {
        plugin = this;

        mongoManager = new MongoManager("mongodb://127.0.0.1:27017/");
        mongoManager.connectToDatabase("BloodMC");

        masterConnection = new MasterConnection("localhost", MASTER_PORT);
        masterConnection.startListeningForPackets();

        cloudMsgCommand = new CloudMsgCommand("cloudmessage");
        banMsgCommand = new BanMsgCommand("banmessage");

        registerEvents();
        registerCommands();

        getLogger().info("Bungeecord-Plugin gestartet");
    }

    @Override
    public void onDisable() {
        masterConnection.stopListening();

        getLogger().info("Bungeecord-Plugin gestoppt");
    }

    private void registerEvents() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinEvent());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new SpigotConnection());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new OnChatEvent());
    }

    private void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, cloudMsgCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(this, banMsgCommand);
    }

    public static BungeePlugin getPlugin() {
        return plugin;
    }

    public static void sendMessage(String msg) {
        getPlugin().getLogger().info(Cprefix + msg);
    }

    public MasterConnection getMasterConnection() {
        return masterConnection;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public CloudMsgCommand getCloudMsgCommand() {
        return cloudMsgCommand;
    }

    public BanMsgCommand getBanMsgCommand() {
        return banMsgCommand;
    }
}
