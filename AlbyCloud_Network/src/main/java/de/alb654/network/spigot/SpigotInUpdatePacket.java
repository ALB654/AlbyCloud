package de.alb654.network.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 28.6.2020
 */

import de.alb654.network.Packet;

public class SpigotInUpdatePacket extends Packet {

    private final String serverId;
    private final String motd;
    private final int onlinePlayer;
    private final int maxPlayer;
    private final int serverState;

    public SpigotInUpdatePacket(String serverId, String motd, int onlinePlayer, int maxPlayer, int serverState) {
        super("SPIGOT_UPDATE");
        this.serverId = serverId;
        this.motd = motd;
        this.onlinePlayer = onlinePlayer;
        this.maxPlayer = maxPlayer;
        this.serverState = serverState;
    }

    public String getServerId() {
        return serverId;
    }

    public int getServerState() {
        return serverState;
    }

    public int getOnlinePlayer() {
        return onlinePlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public String getMotd() {
        return motd;
    }
}
