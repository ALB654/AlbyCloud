package de.alb654.AlbyBungeePlugin.network.handle.handler.master;

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.network.handle.PacketHandler;
import de.alb654.network.bungee.BungeeOutCloudMessagePacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/*
 * Copyright (c) 
 * Created by Aaron Bernert on 18.7.2020
 */

public class BungeeCloudMessageHandler implements PacketHandler<BungeeOutCloudMessagePacket> {

    @Override
    public void handle(BungeeOutCloudMessagePacket packet) {
        for(String playerUUID : BungeePlugin.getPlugin().getCloudMsgCommand().getReceiver()) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(playerUUID));
            player.sendMessage(BungeePlugin.BCprefix + "§7" + packet.getMsg());
        }
    }
}
