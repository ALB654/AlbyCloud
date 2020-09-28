package de.alb654.AlbySpigotPlugin.commands;/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.9.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.network.spigot.SpigotInCloudCommandPacket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CloudCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] args) {

        if(commandSender.hasPermission("BloodMC.CloudCommands")) {

            if(args.length <= 1) {
                commandSender.sendMessage(CloudApi.Bprefix + "§c/cloud <cmd>");
                return true;
            }

            String command = args[0];

            if(command.equalsIgnoreCase("help") ||
                    command.equalsIgnoreCase("hilfe") ||
                    command.equalsIgnoreCase("switchScreen") ||
                    command.equalsIgnoreCase("switch") ||
                    command.equalsIgnoreCase("ss") ||
                    command.equalsIgnoreCase("screen")) {
                commandSender.sendMessage(CloudApi.Bprefix + "§cDieser Cloud-Command wird nicht als ingame-Command unterstützt!");
                return true;
            }

            String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);

            SpigotPlugin.getSpigotPlugin().getMasterConnection().send(
                    new SpigotInCloudCommandPacket(command, cmdArgs, commandSender instanceof Player ? ((Player)commandSender).getUniqueId().toString() : null));
        } else {
            commandSender.sendMessage(CloudApi.Bprefix + "§cDafür hast du keine Berechtigung!");
        }
        return true;
    }
}
