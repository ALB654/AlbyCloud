package de.alb654.network.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.network.Packet;

public class SpigotOutCommandPacket extends Packet {

    private final String[] args;

    public SpigotOutCommandPacket(String[] args) {
        super("SPIGOT_COMMAND");
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }
}
