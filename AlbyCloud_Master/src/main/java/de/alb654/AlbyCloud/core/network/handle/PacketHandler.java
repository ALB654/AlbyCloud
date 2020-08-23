package de.alb654.AlbyCloud.core.network.handle;

import de.alb654.network.Packet;

import java.net.Socket;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 20.6.2020
 */
public interface PacketHandler<T extends Packet> {

    void handle(T packet, Socket socket);

}
