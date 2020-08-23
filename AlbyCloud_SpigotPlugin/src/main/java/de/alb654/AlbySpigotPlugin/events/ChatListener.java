package de.alb654.AlbySpigotPlugin.events;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 18.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.ban.BanPlayer;
import de.alb654.AlbySpigotPlugin.ban.BanReason;
import de.alb654.AlbySpigotPlugin.otherManager.BlackListManager;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import de.alb654.AlbySpigotPlugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String msg = e.getMessage().toLowerCase();
        if(!player.hasPermission("BloodMC.Blacklist")) {
            BlackListManager manager = SpigotPlugin.getSpigotPlugin().getBlackListManager();
            if(manager.replaceNumbersAndcheckIfContainsWord(msg) || manager.containsBlacklistedWord(msg)) {
                e.setCancelled(true);
                SpigotPlugin.getSpigotPlugin().getBanManager().banPlayer(player.getName(), BanReason.createCustom("Wortwahl", 1000 * 60, true), "SYSTEM");
            }
        }
        if(msg.startsWith("/"))
            return;

        if(!player.hasPermission("BloodMC.Werbung")) {
            if(msg.contains("https://") || msg.contains("http://") || msg.contains("www.") || msg.contains(".com") || msg.contains(".de") ||
                msg.contains(".eu") || msg.contains(".net") || msg.contains(".at") || msg.contains(".us") || msg.contains(".org")) {
                e.setCancelled(true);
                player.sendMessage(CloudApi.Bprefix + "§c§lBITTE SCHREIBE KEINE LINKS IN DEN CHAT");
                for(Player all : Bukkit.getOnlinePlayers()) {
                    if(all.hasPermission("BloodMC.Werbung")) {
                        all.sendMessage(CloudApi.Bprefix + " ");
                        all.sendMessage(CloudApi.Bprefix + "§c" + player.getName() + " §7versuchte §b'" + msg + "' §7zu schreiben.");
                        all.sendMessage(CloudApi.Bprefix + " ");
                    }
                }
            }
        }

        String uuid = NameFetcher.getUUID(player.getName()).toString();
        if(SpigotPlugin.getSpigotPlugin().getBanManager().isBanned(uuid)) {
            BanPlayer banPlayer = SpigotPlugin.getSpigotPlugin().getBanManager().getBannedPlayer(uuid);
            if(banPlayer.isMute()) {
                long untilTime = banPlayer.getUntilTime();
                if(untilTime != -1 && System.currentTimeMillis() - untilTime >= 0)
                    SpigotPlugin.getSpigotPlugin().getBanManager().unbanPlayer(uuid, "SYSTEM");
                else {
                    e.setCancelled(true);
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    if (untilTime == -1)
                        player.sendMessage(CloudApi.Bprefix + "§cDu wurdest §4PERMANENT §cgemutet!");
                    else
                        player.sendMessage(CloudApi.Bprefix + "§cDu wurdest gemutet!");
                    player.sendMessage(CloudApi.Bprefix + "§bGrund§7: §b" + banPlayer.getReason());
                    if (untilTime != -1)
                        player.sendMessage(CloudApi.Bprefix + "§bübrige Zeit§7: §b" + Utils.toTimeString(banPlayer.getUntilTime() - System.currentTimeMillis()));
                    player.sendMessage(" ");
                }
            }
        }
    }
}
