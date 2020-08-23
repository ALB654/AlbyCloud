package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import de.alb654.AlbySpigotPlugin.inventory.inventorys.perm.PermGroupListGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("BloodMC.Permissions")) {
                new PermGroupListGUI(player);
            }
        }

        return true;
    }

}
