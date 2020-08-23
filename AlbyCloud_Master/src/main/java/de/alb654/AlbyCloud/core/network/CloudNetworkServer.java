package de.alb654.AlbyCloud.core.network;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 20.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.network.handle.PacketHandler;
import de.alb654.AlbyCloud.core.network.handle.spigot.SpigotMovePlayersHandle;
import de.alb654.AlbyCloud.core.network.handle.spigot.SpigotUpdateHandle;
import de.alb654.network.spigot.SpigotInLoginPacket;

public class CloudNetworkServer extends NetworkServer {

    {
        addPacketHandler("BUNGEE_LOGIN", (packet, socket) -> {
            addClient("Bungee", socket);
            Main.getAlbyCloud().getBungeeServer().confirmStart();
        });

        addPacketHandler("SPIGOT_LOGIN", (PacketHandler<SpigotInLoginPacket>) (packet, socket) -> {
            addClient(packet.getServerID(), socket);
            Main.getAlbyCloud().getServerManager().confirmServerStart(packet.getServerID());
        });

        addPacketHandler("SPIGOT_UPDATE", new SpigotUpdateHandle());
        addPacketHandler("SPIGOT_MOVE_PLAYERS", new SpigotMovePlayersHandle());
    }

    public CloudNetworkServer(int port) {
        super(port);
    }

}
