package de.alb654.network.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 31.8.2020
 */

import de.alb654.network.Packet;

public class SpigotInSafeWorldAsTemplatePacket extends Packet {

    private final String serverID;

    public SpigotInSafeWorldAsTemplatePacket(String serverID) {
        super("SPIGOT_SAFE_WORLD_AS_TEMPLATE");

        this.serverID = serverID;
    }

    public String getServerID() {
        return serverID;
    }
}
