package de.alb654.AlbyBungeePlugin.network.handle;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 21.6.2020
 */

import de.alb654.network.Packet;

public interface PacketHandler<T extends Packet> {

    void handle(T packet);

}
