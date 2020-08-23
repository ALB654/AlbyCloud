package de.alb654.network.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.7.2020
 */

import de.alb654.network.Packet;

public class SpigotInMovePlayersPacket extends Packet {

    private final String[] uuids;
    private final String serverID;

    public SpigotInMovePlayersPacket(String[] playerUUIDs, String serverID) {
        super("SPIGOT_MOVE_PLAYERS");
        uuids = playerUUIDs;
        this.serverID = serverID;
    }

    public String[] getUUIDS() {
        return uuids;
    }

    public String getServerID() {
        return serverID;
    }
}
