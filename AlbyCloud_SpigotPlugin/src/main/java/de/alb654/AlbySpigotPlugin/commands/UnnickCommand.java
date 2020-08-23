package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 31.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.nick.NickManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnnickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("BloodMC.Nick")) {
                NickManager nickManager = SpigotPlugin.getSpigotPlugin().getNickManager();
                if(nickManager.isNickedInDB(player.getUniqueId().toString())) {
                    nickManager.removeNickFromDB(player.getUniqueId().toString());
                    nickManager.realizeNick(player);
                    player.sendMessage(CloudApi.Bprefix + "§aDu bist nichtmehr genickt!");
                } else {
                    player.sendMessage(CloudApi.Bprefix + "§cDu bist nicht genickt!");
                }
            }
        }

        return true;
    }
}
