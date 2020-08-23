package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 21.6.2020
 */

import de.alb654.network.Packet;

public class BungeeOutShutdownPacket extends Packet {

    public BungeeOutShutdownPacket() {
        super("BUNGEE_SHUTDOWN");
    }

}
