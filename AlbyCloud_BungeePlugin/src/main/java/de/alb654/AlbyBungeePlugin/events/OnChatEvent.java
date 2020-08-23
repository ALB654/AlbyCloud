package de.alb654.AlbyBungeePlugin.events;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.8.2020
 */

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnChatEvent implements Listener {

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
        String msg = e.getMessage();
        if(sender.hasPermission("BloodMC.TeamChat")) {
            if(msg.startsWith("@t")) {
                ProxyServer.getInstance().getPlayers().forEach(player -> {
                    if(player.hasPermission("BloodMC.TeamChat")){
                        player.sendMessage(BungeePlugin.BprefixWithoutDot + "§c§lTEAM " + BungeePlugin.DOT + " §a"
                                + msg.replace("@t", ""));
                    }
                });
                e.setCancelled(true);
            }
        }
    }

}
