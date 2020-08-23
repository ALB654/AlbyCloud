package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 18.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.otherManager.BlackListManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlacklistCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender.hasPermission("BloodMC.Blacklist")) {
            BlackListManager manager = SpigotPlugin.getSpigotPlugin().getBlackListManager();
            if(args.length == 1) {

                if(manager.isBlacklisted(args[0]))
                    commandSender.sendMessage(CloudApi.Bprefix + "§aDieses Word ist auf der Blacklist.");
                else
                    commandSender.sendMessage(CloudApi.Bprefix + "§cDieses Word ist nicht auf der Blacklist.");
                return true;

            } else if(args.length == 2) {

                if(args[0].equalsIgnoreCase("add")) {
                    if(manager.isBlacklisted(args[1])) {
                        commandSender.sendMessage(CloudApi.Bprefix + "§cDieses Word ist bereits auf der Blacklist.");
                    } else {
                        manager.addBlacklistesWord(args[1]);
                        commandSender.sendMessage(CloudApi.Bprefix + "§b'" + args[1] + "' §7zur Blacklist hinzugefügt.");
                    }

                } else if(args[0].equalsIgnoreCase("remove")) {
                    if(manager.isBlacklisted(args[1])) {
                        manager.removeBlacklistedWord(args[1]);
                        commandSender.sendMessage(CloudApi.Bprefix + "§b'" + args[1] + "' §7von Blacklist entfernt.");
                    } else
                        commandSender.sendMessage(CloudApi.Bprefix + "§cDieses Word ist nicht auf der Blacklist.");
                }
            } else {
                commandSender.sendMessage(CloudApi.Bprefix + "§c/balcklist add/remove <word>");
            }
        }
        return true;
    }

}
