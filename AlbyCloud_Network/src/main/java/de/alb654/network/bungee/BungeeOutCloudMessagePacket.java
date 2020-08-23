package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 7.7.2020
 */

import de.alb654.network.Packet;

public class BungeeOutCloudMessagePacket extends Packet {

    private final String msg;

    public BungeeOutCloudMessagePacket(String msg) {
        super("BUNGEE_CLOUD_MESSAGE");
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
