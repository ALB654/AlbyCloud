package de.alb654.AlbySpigotPlugin.commands;/*
 * Copyright (c)
 * Created by Aaron Bernert on 24.8.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.inventory.inventorys.report.ActiveReportGui;
import de.alb654.AlbySpigotPlugin.report.ReportCase;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(args.length > 0) {
                if(args.length == 2) {
                    String name = args[0];
                    String reason = args[1];

                    UUID uuid = NameFetcher.getUUID(name);
                    if(uuid != null) {
                        if(!uuid.equals(player.getUniqueId())) {
                            SpigotPlugin.getSpigotPlugin().getReportManager().addReport(uuid.toString(), player.getUniqueId().toString(), reason);
                            player.sendMessage(CloudApi.Bprefix + "§7Vielen Dank für deinen Report! Wir werden uns darum kümmern.");
                        } else
                            player.sendMessage(CloudApi.Bprefix + "§cDu kannst dich nicht selber reporten!");

                    } else
                        player.sendMessage(CloudApi.Bprefix + "§cDieser Spieler existiert nicht!");

                } else
                    player.sendMessage(CloudApi.Bprefix + "§c/report <name> <grund>");
            } else if(player.hasPermission("BloodMC.Reports")) {
                ReportCase rCase = SpigotPlugin.getSpigotPlugin().getReportManager().getActiveReportFrom(player.getUniqueId().toString());
                if(rCase == null) {
                    player.sendMessage(CloudApi.Bprefix + "§cDu beobachtest gerade keinen Spieler");
                    return true;
                }

                new ActiveReportGui(player, rCase);
            } else {
                player.sendMessage(CloudApi.Bprefix + "§c/report <name> <grund>");
            }
        }
        return true;
    }

}
