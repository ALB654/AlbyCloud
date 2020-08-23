package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.network.Packet;

public class BungeeOutCommandPacket extends Packet {

    private final String[] args;

    public BungeeOutCommandPacket(String... args) {
        super("BUNGEE_COMMAND");
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }
}
