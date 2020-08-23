package de.alb654.AlbySpigotPlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.7.2020
 */

import de.alb654.AlbySpigotPlugin.inventory.inventorys.ban.PlayerBanMenuGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BansCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("BloodMC.Ban")) {
                new PlayerBanMenuGUI(player);
            } else
                player.performCommand("wpeizge");
        }

        return true;
    }

}
