package de.alb654.AlbySpigotPlugin;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.ban.BanManager;
import de.alb654.AlbySpigotPlugin.commands.*;
import de.alb654.AlbySpigotPlugin.events.ChatListener;
import de.alb654.AlbySpigotPlugin.otherManager.BlackListManager;
import de.alb654.AlbySpigotPlugin.nick.NickManager;
import de.alb654.AlbySpigotPlugin.recent.RecentActionsManager;
import de.alb654.AlbySpigotPlugin.events.ChatGetTextManager;
import de.alb654.AlbySpigotPlugin.events.InventoryListener;
import de.alb654.AlbySpigotPlugin.events.JoinAndQuitListener;
import de.alb654.AlbySpigotPlugin.inventory.BloodInvManager;
import de.alb654.AlbySpigotPlugin.network.MasterConnection;
import de.alb654.AlbySpigotPlugin.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SpigotPlugin extends JavaPlugin {

    private static SpigotPlugin instance;

    private String serverID;

    private MasterConnection masterConnection;
    private MongoManager mongoManager;
    private BloodInvManager bloodInvManager;
    private ChatGetTextManager chatGetTextManager;
    private BanManager banManager;
    private RecentActionsManager recentActionsManager;
    private BlackListManager blackListManager;
    private NickManager nickManager;
    private ReportManager reportManager;

    @Override
    public void onEnable() {
        instance = this;
        getAndSetServerID();

        new CloudApi();

        mongoManager = new MongoManager("mongodb://127.0.0.1:27017/");
        mongoManager.connectToDatabase("BloodMC");

        masterConnection = new MasterConnection("localhost", 9977);
        masterConnection.startListeningForPackets();

        bloodInvManager = new BloodInvManager();
        chatGetTextManager = new ChatGetTextManager();

        banManager = new BanManager(mongoManager);
        reportManager = new ReportManager(mongoManager);
        recentActionsManager = new RecentActionsManager(mongoManager);

        blackListManager = new BlackListManager(mongoManager);
        nickManager = new NickManager(this);

        registerListener();
        registerCommands();

        sendMessage("SpigotPlugin gestartet!");
    }

    @Override
    public void onDisable() {
        masterConnection.stopListening();
        sendMessage("SpigotPlugin gestoppt!");
    }

    private void registerCommands() {
        getCommand("Perms").setExecutor(new PermCommand());
        getCommand("bans").setExecutor(new BansCommand());
        getCommand("jump").setExecutor(new JumpCommand());
        getCommand("blacklist").setExecutor(new BlacklistCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("nick").setExecutor(new NickCommand());
        getCommand("unnick").setExecutor(new UnnickCommand());
        getCommand("reports").setExecutor(new ReportsCommand());
        getCommand("report").setExecutor(new ReportCommand());
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new JoinAndQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

        Bukkit.getPluginManager().registerEvents(chatGetTextManager, this);
    }

    private void getAndSetServerID() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new File("plugins/SpigotPlugin/Name.yml"));
        serverID = configuration.getString("Name");
    }

    public static SpigotPlugin getSpigotPlugin() {
        return instance;
    }

    public static void sendMessage(String msg) {
        instance.getLogger().info(msg);
    }

    public String getServerID() {
        return serverID;
    }

    public MasterConnection getMasterConnection() {
        return masterConnection;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public BloodInvManager getBloodInvManager() {
        return bloodInvManager;
    }

    public ChatGetTextManager getChatGetTextManager() {
        return chatGetTextManager;
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public RecentActionsManager getRecentActionsManager() {
        return recentActionsManager;
    }

    public BlackListManager getBlackListManager() {
        return blackListManager;
    }

    public NickManager getNickManager() {
        return nickManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }
}
