package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 17.7.2020
 */

import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.api.server.CloudServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JumpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("BloodMC.Jump")) {
                if(args.length != 1) {
                    player.sendMessage(CloudApi.Bprefix + "§c/jump <Server>");
                    return true;
                }
                CloudServer cs = CloudApi.getInstance().getServerManager().getServer(args[0]);
                if(cs != null) {
                    player.sendMessage(CloudApi.Bprefix + "Springe zum Server §a" + cs.getId() + "§7.");
                    CloudApi.getInstance().getServerManager().sendPlayerToServer(player, cs.getId());
                } else
                    player.sendMessage(CloudApi.Bprefix + "§cDieser Server ist nicht online!");
            }
        }
        return true;
    }

}
