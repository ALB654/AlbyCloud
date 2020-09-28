package de.alb654.AlbySpigotPlugin.api;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 29.6.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.server.ServerManager;
import de.alb654.AlbySpigotPlugin.events.JoinAndQuitListener;
import de.alb654.AlbySpigotPlugin.network.BungeeConnection;
import de.alb654.AlbySpigotPlugin.nick.NickManager;

public class CloudApi {

    private static CloudApi instance;

    public static final String prefix = "§bBloodCloud §8• §7";
    public static final String Bprefix = "§b§lBloodMC §8• §7";

    private final ServerManager serverManager;
    private final BungeeConnection bungeeConnection;

    public CloudApi() {
        instance = this;

        serverManager = new ServerManager();
        bungeeConnection = new BungeeConnection();
    }

    public void setTablistColor(String colorCode){
        JoinAndQuitListener.tablistColor = colorCode;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public BungeeConnection getBungeeConnection() {
        return bungeeConnection;
    }

    public NickManager getNickManager() {
        return getCloud().getNickManager();
    }

    public SpigotPlugin getCloud() {
        return SpigotPlugin.getSpigotPlugin();
    }

    public static CloudApi getInstance() {
        return instance;
    }
}
