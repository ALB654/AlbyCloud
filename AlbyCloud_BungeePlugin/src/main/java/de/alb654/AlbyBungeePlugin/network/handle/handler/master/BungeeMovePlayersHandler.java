package de.alb654.AlbyBungeePlugin.network.handle.handler.master;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.7.2020
 */

import de.alb654.AlbyBungeePlugin.network.handle.PacketHandler;
import de.alb654.network.bungee.BungeeOutMovePlayersPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeMovePlayersHandler implements PacketHandler<BungeeOutMovePlayersPacket> {
    @Override
    public void handle(BungeeOutMovePlayersPacket packet) {
        ServerInfo server = ProxyServer.getInstance().getServerInfo(packet.getServerId());

        if(server != null) {
            for (String uuid : packet.getUUIDS()) {
                ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(UUID.fromString(uuid));
                if (pp != null) {
                    pp.connect(server);
                }
            }
        }
    }
}
