package de.alb654.network.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 29.6.2020
 */

import de.alb654.network.Packet;

public class SpigotOutUpdatePacket extends Packet {

    private final String serverId;
    private final String motd;
    private final int onlinePlayer;
    private final int maxPlayer;
    private final int serverState;

    private final boolean running;

    public SpigotOutUpdatePacket(SpigotInUpdatePacket reverse) {
        this(reverse.getServerId(), reverse.getMotd(), reverse.getOnlinePlayer(), reverse.getMaxPlayer(), reverse.getServerState(), true);
    }

    public SpigotOutUpdatePacket(String serverId, String motd, int onlinePlayer, int maxPlayer, int serverState, boolean running) {
        super("SPIGOT_UPDATE");
        this.serverId = serverId;
        this.motd = motd;
        this.onlinePlayer = onlinePlayer;
        this.maxPlayer = maxPlayer;
        this.serverState = serverState;
        this.running = running;
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

    public boolean isRunning() {
        return running;
    }
}
