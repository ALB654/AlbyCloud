package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.7.2020
 */

import de.alb654.network.Packet;

public class BungeeOutMovePlayersPacket extends Packet {

    private final String[] uuids;
    private final String serverId;

    public BungeeOutMovePlayersPacket(String[] playerUUIDs, String serverId) {
        super("BUNGEE_MOVE_PLAYERS");
        uuids = playerUUIDs;
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    public String[] getUUIDS() {
        return uuids;
    }
}
