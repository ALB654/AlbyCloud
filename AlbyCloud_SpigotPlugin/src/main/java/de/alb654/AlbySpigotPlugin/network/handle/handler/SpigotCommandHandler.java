package de.alb654.AlbySpigotPlugin.network.handle.handler;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.network.handle.PacketHandler;
import de.alb654.network.spigot.SpigotOutCommandPacket;
import org.bukkit.Bukkit;

public class SpigotCommandHandler implements PacketHandler<SpigotOutCommandPacket> {

    @Override
    public void handle(SpigotOutCommandPacket packet) {
        StringBuilder line = new StringBuilder();
        for(String s : packet.getArgs())
            line.append(s).append(" ");
        Bukkit.getScheduler().callSyncMethod(SpigotPlugin.getSpigotPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.toString()));
    }
}
