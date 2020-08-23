package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.nick.NickManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("BloodMC.Nick")) {
                if(args.length != 1) {
                    player.sendMessage(CloudApi.Bprefix + "§c/nick <name>");
                    return true;
                }
                String nick = args[0];
                NickManager manager = SpigotPlugin.getSpigotPlugin().getNickManager();
                String uuid = player.getUniqueId().toString();

                if(manager.isNickedInDB(uuid)) {
                    manager.removeNickFromDB(uuid);
                }

                manager.nickPlayerInDB(uuid, nick);
                manager.realizeNick(player);

                player.sendMessage(CloudApi.Bprefix + "Du bist jetzt §a" + nick + "§7!");
            } else
                player.performCommand("aoizgfoai");
        }

        return true;
    }
}
