package de.alb654.AlbySpigotPlugin.network.handle.handler;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 29.6.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.api.server.CloudServer;
import de.alb654.AlbySpigotPlugin.api.server.ServerManager;
import de.alb654.AlbySpigotPlugin.network.handle.PacketHandler;
import de.alb654.network.spigot.SpigotOutUpdatePacket;

public class SpigotUpdateHandler implements PacketHandler<SpigotOutUpdatePacket> {

    @Override
    public void handle(SpigotOutUpdatePacket packet) {
        ServerManager manager = CloudApi.getInstance().getServerManager();

        CloudServer server = manager.getServer(packet.getServerId());

        if(server != null) {
            if(server.getId().equals(SpigotPlugin.getSpigotPlugin().getServerID()))
                return;

            if(!packet.isRunning()) {
                manager.removeServer(server);
                return;
            }

            server.setMaxPlayer(packet.getMaxPlayer());
            server.setMotd(packet.getMotd());
            server.setOnlinePlayer(packet.getOnlinePlayer());
            server.setState(CloudServer.State.fromId(packet.getServerState()));
        } else {
            manager.addCloudServer(
                    new CloudServer(packet.getServerId(), packet.getMotd(), packet.getOnlinePlayer(), packet.getMaxPlayer(), CloudServer.State.fromId(packet.getServerState())));
        }
    }
}
