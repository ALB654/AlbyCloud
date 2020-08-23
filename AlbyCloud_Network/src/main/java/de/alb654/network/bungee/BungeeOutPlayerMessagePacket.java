package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 7.7.2020
 */

import de.alb654.network.Packet;

public class BungeeOutPlayerMessagePacket extends Packet {

    private final String playerUUID;
    private final String msg;

    public BungeeOutPlayerMessagePacket(String playerUUID, String msg) {
        super("BUNGEE_PLAYER_MESSAGE");
        this.playerUUID = playerUUID;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }
}
