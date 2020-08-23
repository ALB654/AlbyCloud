package de.alb654.AlbyBungeePlugin.network.handle.handler.master;

import de.alb654.AlbyBungeePlugin.network.handle.PacketHandler;
import de.alb654.network.bungee.BungeeOutCommandPacket;
import net.md_5.bungee.api.ProxyServer;

/*
 * Copyright (c) 
 * Created by Aaron Bernert on 18.7.2020
 */

public class BungeeCommandHandler implements PacketHandler<BungeeOutCommandPacket> {

    @Override
    public void handle(BungeeOutCommandPacket packet) {
        StringBuilder line = new StringBuilder();
        for(String s : packet.getArgs())
            line.append(s).append(" ");
        String cmd = line.toString();
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), cmd);
    }
}
