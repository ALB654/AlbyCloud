package de.alb654.AlbySpigotPlugin.network.handle;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.network.Packet;

public interface PacketHandler<T extends Packet> {

    void handle(T packet);

}
