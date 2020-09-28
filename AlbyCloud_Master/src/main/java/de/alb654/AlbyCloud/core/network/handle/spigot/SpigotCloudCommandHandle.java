package de.alb654.AlbyCloud.core.network.handle.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.9.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.network.handle.PacketHandler;
import de.alb654.AlbyCloud.screens.ScreenManager;
import de.alb654.network.bungee.BungeeOutPlayerMessagePacket;
import de.alb654.network.spigot.SpigotInCloudCommandPacket;

import java.net.Socket;

public class SpigotCloudCommandHandle implements PacketHandler<SpigotInCloudCommandPacket> {

    @Override
    public void handle(SpigotInCloudCommandPacket packet, Socket socket) {
        String label = packet.getCommand() + " ";
        for(String arg : packet.getArgs()) {
            label += arg + " ";
        }
        label = label.trim();

        Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).executeCommand(packet.getCommand(), packet.getArgs(), label);
        Main.getAlbyCloud().getCloudNetworkServer().sendPacket("Bungee", new BungeeOutPlayerMessagePacket(packet.getPlayerUUID(), "Command '" + packet.getCommand() + "' auseführt!"));
    }

}
