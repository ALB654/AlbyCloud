package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 20.6.2020
 */

import de.alb654.network.Packet;

public class BungeeInLoginPacket extends Packet {

    public BungeeInLoginPacket() {
        super("BUNGEE_LOGIN");
    }
}
