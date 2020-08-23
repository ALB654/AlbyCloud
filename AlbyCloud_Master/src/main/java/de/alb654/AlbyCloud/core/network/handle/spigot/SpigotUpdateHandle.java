package de.alb654.AlbyCloud.core.network.handle.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 29.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.network.handle.PacketHandler;
import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.screens.ScreenManager;
import de.alb654.network.spigot.SpigotInUpdatePacket;
import de.alb654.network.spigot.SpigotOutUpdatePacket;

import java.net.Socket;

public class SpigotUpdateHandle implements PacketHandler<SpigotInUpdatePacket> {

    @Override
    public void handle(SpigotInUpdatePacket packet, Socket socket) {
        Server server = Main.getAlbyCloud().getServerManager().getServer(packet.getServerId());
        if(server == null) {
            Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).addMsg("SpigotUpdate von '" + packet.getServerId() + "'empfangen, obwohl Server nicht existiert");
            return;
        }

        server.setMaxPlayers(packet.getMaxPlayer());
        server.setOnlinePlayers(packet.getOnlinePlayer());
        server.setMotd(packet.getMotd());
        server.setServerState(Server.State.fromId(packet.getServerState()));

        Main.getAlbyCloud().getCloudNetworkServer().sendToAll(new SpigotOutUpdatePacket(packet));
    }
}
