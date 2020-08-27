package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 19.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.ban.BanReason;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.UUID;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender.hasPermission("BloodMC.Ban")) {
            if(args.length != 2) {
                commandSender.sendMessage(CloudApi.Bprefix + "§c/ban <player> <reason>");
                return true;
            }
            UUID uuid = NameFetcher.getUUID(args[0]);
            if(uuid == null) {
                commandSender.sendMessage(CloudApi.Bprefix + "§cDieser Spieler existiert nicht!");
                return true;
            }
            BanReason br = SpigotPlugin.getSpigotPlugin().getBanManager().getBanReason(args[1]);
            if(br == null) {
                commandSender.sendMessage(CloudApi.Bprefix + "§cDieser Bangrund existiert nicht!");
                return true;
            }
            if(SpigotPlugin.getSpigotPlugin().getBanManager().banPlayer(StringUtils.capitalize(args[0].toLowerCase()), br, commandSender.getName()))
                commandSender.sendMessage(CloudApi.Bprefix + "§aSpieler bestraft.");
            else
                commandSender.sendMessage(CloudApi.Bprefix + "§cSpieler ist bereits gebannt/gemuted.");
        }
        return true;
    }
}
