package de.alb654.network.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.network.Packet;

public class SpigotInLoginPacket extends Packet {

    private final String serverID;

    public SpigotInLoginPacket(String serverID) {
        super("SPIGOT_LOGIN");
        this.serverID = serverID;
    }

    public String getServerID() {
        return serverID;
    }
}
