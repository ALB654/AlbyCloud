package de.alb654.AlbyCloud.core.network.handle.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.network.handle.PacketHandler;
import de.alb654.network.spigot.SpigotInLoginPacket;

import java.net.Socket;

public class SpigotLoginHandle implements PacketHandler<SpigotInLoginPacket> {

    @Override
    public void handle(SpigotInLoginPacket packet, Socket socket) {
        Main.getAlbyCloud().getServerManager().confirmServerStart(packet.getServerID());
    }
}
