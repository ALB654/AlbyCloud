package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.network.Packet;

public class BungeeOutRemoveServerPacket extends Packet {

    private String id;

    public BungeeOutRemoveServerPacket(String id) {
        super("BUNGEE_REMOVE_SERVER");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
