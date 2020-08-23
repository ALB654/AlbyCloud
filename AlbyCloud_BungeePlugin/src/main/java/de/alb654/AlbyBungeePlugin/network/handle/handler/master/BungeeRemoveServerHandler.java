package de.alb654.AlbyBungeePlugin.network.handle.handler.master;

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.network.handle.PacketHandler;
import de.alb654.network.bungee.BungeeOutRemoveServerPacket;
import net.md_5.bungee.api.ProxyServer;

/*
 * Copyright (c) 
 * Created by Aaron Bernert on 18.7.2020
 */

public class BungeeRemoveServerHandler implements PacketHandler<BungeeOutRemoveServerPacket> {

    @Override
    public void handle(BungeeOutRemoveServerPacket packet) {
        ProxyServer.getInstance().getServers().remove(packet.getId());
        BungeePlugin.sendMessage("Server '" + packet.getId() + "' entfernt");
    }
}
