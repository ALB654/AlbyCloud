package de.alb654.AlbyCloud.core.network.handle.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.7.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.network.handle.PacketHandler;
import de.alb654.network.bungee.BungeeOutMovePlayersPacket;
import de.alb654.network.spigot.SpigotInMovePlayersPacket;

import java.net.Socket;

public class SpigotMovePlayersHandle implements PacketHandler<SpigotInMovePlayersPacket> {

    @Override
    public void handle(SpigotInMovePlayersPacket packet, Socket socket) {
        Main.getAlbyCloud().getCloudNetworkServer().sendPacket("Bungee", new BungeeOutMovePlayersPacket(packet.getUUIDS(), packet.getServerID()));
    }
}
