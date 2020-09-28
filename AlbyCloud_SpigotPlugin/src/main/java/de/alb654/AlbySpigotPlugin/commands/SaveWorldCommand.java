package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 31.8.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.network.spigot.SpigotInSafeWorldAsTemplatePacket;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("BloodMC.Safe")) {
                Bukkit.getWorlds().forEach(World::save);
                SpigotPlugin.getSpigotPlugin().getMasterConnection().send(
                        new SpigotInSafeWorldAsTemplatePacket(CloudApi.getInstance().getServerManager().getThisServer().getId()));
                player.sendMessage(CloudApi.Bprefix + "§7Speichere Welt als Template... (Solange es kein Static-Server ist)");
            } else
                player.performCommand("asdfafwrgwrgAqef124");
        }
        return true;
    }

}
