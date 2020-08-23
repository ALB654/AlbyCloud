package de.alb654.AlbyBungeePlugin.network.handle.handler.spigot;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 18.7.2020
 */

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.network.SpigotConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BanKickHandler implements SpigotConnection.PluginMessageHandler {

    @Override
    public void handleMessage(String[] args) {
        ProxiedPlayer suspect = ProxyServer.getInstance().getPlayer(args[0]);
        if(suspect != null) {
            suspect.disconnect(
                    "§7• §b§lBloodMC §7•\n" +
                      "\n" +
                      "§c§lDu wurdest gebannt!\n" +
                      "\n" +
                      "§7Grund: §b" + args[1] + "\n" +
                      "\n" +
                      "§8Du wurdest zuunrecht gebannt? Du kannst hier einen Entbannungsantrag stellen:\n" +
                      "§7Discord: §bdiscord.BloodMC.eu \n" +
                      "§7Forum: §bforum.BloodMC.eu"
            );
        }

        BungeePlugin.getPlugin().getBanMsgCommand().getReceiver().forEach(uuid -> {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(uuid));
            if(player != null) {
                player.sendMessage("  ");
                player.sendMessage(BungeePlugin.Bprefix + "========================================================");
                player.sendMessage(BungeePlugin.Bprefix + "§a" + suspect.getName() + " §7wurde für §a" + args[1] + " §7gebannt!");
                player.sendMessage(BungeePlugin.Bprefix + "========================================================");
                player.sendMessage("  ");
            }
        });
    }

}
