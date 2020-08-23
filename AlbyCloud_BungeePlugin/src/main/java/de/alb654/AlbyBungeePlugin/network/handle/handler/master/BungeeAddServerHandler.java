package de.alb654.AlbyBungeePlugin.network.handle.handler.master;

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.network.handle.PacketHandler;
import de.alb654.network.bungee.BungeeOutAddServerPacket;
import net.md_5.bungee.api.ProxyServer;

/*
 * Copyright (c) 
 * Created by Aaron Bernert on 18.7.2020
 */

public class BungeeAddServerHandler implements PacketHandler<BungeeOutAddServerPacket> {

    @Override
    public void handle(BungeeOutAddServerPacket p) {
        ProxyServer.getInstance().getServers().put(p.getServerID(), ProxyServer.getInstance().constructServerInfo(p.getServerID(), p.getAddress(), p.getMotd(), false));
        BungeePlugin.sendMessage("BungeeServer '" + p.getServerID() + "' hinzugefügt! Port: " + p.getAddress().getPort());
    }

}
