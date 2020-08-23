package de.alb654.AlbyBungeePlugin.network.handle.handler.master;

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.network.handle.PacketHandler;
import de.alb654.network.bungee.BungeeOutPlayerMessagePacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/*
 * Copyright (c) 
 * Created by Aaron Bernert on 18.7.2020
 */

public class BungeePlayerMassageHandler implements PacketHandler<BungeeOutPlayerMessagePacket> {

    @Override
    public void handle(BungeeOutPlayerMessagePacket packet) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(packet.getPlayerUUID()));
        if(player != null){
            player.sendMessage(new ComponentBuilder().append(BungeePlugin.BCprefix).append("§7" + packet.getMsg()).create());
        }
    }

}
