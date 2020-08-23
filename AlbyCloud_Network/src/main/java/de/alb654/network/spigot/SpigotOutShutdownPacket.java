package de.alb654.network.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.network.Packet;

public class SpigotOutShutdownPacket extends Packet {

    public SpigotOutShutdownPacket() {
        super("SPIGOT_SHUTDOWN");
    }

}
