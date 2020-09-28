package de.alb654.AlbyCloud.core.network.handle.spigot;/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.9.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.network.handle.PacketHandler;
import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.core.server.dynamic.DynamicServer;
import de.alb654.network.spigot.SpigotInSafeWorldAsTemplatePacket;

import java.net.Socket;

public class SpigotSafeWorldAsTemplateHandle implements PacketHandler<SpigotInSafeWorldAsTemplatePacket> {

    @Override
    public void handle(SpigotInSafeWorldAsTemplatePacket packet, Socket socket) {
        Server server = Main.getAlbyCloud().getServerManager().getServer(packet.getServerID());
        if(server == null) {
            return;
        }
        if(server instanceof DynamicServer) {
            DynamicServer dynamicServer = (DynamicServer) server;
            dynamicServer.safeCurrentWorldAsTemplate();
        }
    }
}
