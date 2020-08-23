package de.alb654.AlbyBungeePlugin.events;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 23.6.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.mongo.MongoManager;
import de.alb654.AlbyBungeePlugin.utils.NameFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class JoinEvent implements Listener {

    private ArrayList<String> online = new ArrayList<>();

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if(!online.contains(player.getUniqueId().toString())) {
            ServerInfo server = getNextLobbyServer(player);
            if(server != null){
                e.setTarget(server);
                online.add(player.getUniqueId().toString());
            } else {
                player.disconnect(new ComponentBuilder("§7• §b§lBloodMC §7•\n\n")
                        .append("Zurzeit ist keine Lobby erreichbar!\nBitte versuche es in wenigen Minuten erneut.")
                        .color(ChatColor.RED).create());
            }
        }
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent e) {
        MongoCollection<Document> bannedplayers = BungeePlugin.getPlugin().getMongoManager()
                .getCollection(MongoManager.BANNEDPLAYER);

        Document player = bannedplayers.find(Filters.and(Filters.eq("UUID", NameFetcher.getUUID(e.getConnection().getName()).toString()), Filters.eq("isMute", false))).first();
        if(player != null){
            long untilTime = player.getLong("untilTime");
            if(System.currentTimeMillis() >= untilTime && untilTime != -1) {
                bannedplayers.deleteOne(player);
            } else {
                e.setCancelled(true);
                e.setCancelReason(
                        "§7• §b§lBloodMC §7•\n" +
                                "\n" +
                                "§c§lDu wurdest " + (untilTime == -1 ? "§4§lPERMANENT§c" : "") + " gebannt!\n" +
                                "\n" +
                                "§7Grund: §b" + player.get("reason") + "\n" +
                                (untilTime == -1 ? "" : "§7verbleibende Zeit: §b" + toTimeString(untilTime - System.currentTimeMillis(), " Tag(e)", " Stunde(n)", " Minute(n)", " Sekunde(n)") + "\n") +
                                "\n" +
                                "§8Du wurdest zuunrecht gebannt? Du kannst hier einen Entbannungsantrag stellen:\n" +
                                "§7Discord: §bdiscord.BloodMC.eu \n" +
                                "§7Forum: §bforum.BloodMC.eu");
            }
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        player.getPermissions().forEach(perm -> player.setPermission(perm, false));
        getBungeePerms(player.getUniqueId().toString()).forEach(perm -> player.setPermission(perm, true));
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        online.remove(player.getUniqueId().toString());
    }

    @EventHandler
    public void onKick(ServerKickEvent e) {
        ProxiedPlayer player = e.getPlayer();
        ServerInfo si = getNextLobbyServer(player);
        if(si == null) {
            player.disconnect(new ComponentBuilder("§7• §b§lBloodMC §7•\n\n")
                    .append("Zurzeit ist keine Lobby erreichbar!\nBitte versuche es in wenigen Minuten erneut.")
                    .color(ChatColor.RED).create());
        } else {
            player.sendMessage(BungeePlugin.Bprefix + "§cDu wurdest gekickt! Verbinde mit §6" + si.getName() + "§c.");
            e.setCancelled(true);
            e.setCancelServer(si);
        }
    }

    private ServerInfo getNextLobbyServer(ProxiedPlayer player) {
        List<ServerInfo> fallbackServers = new ArrayList<>();
        for(Map.Entry<String, ServerInfo> server : ProxyServer.getInstance().getServers().entrySet()) {
            if(server.getKey().contains("LOBBY") && server.getValue().canAccess(player)){
                if(player.getServer() != null) {
                    if(player.getServer().getInfo().getName().equals(server.getValue().getName()))
                        continue;
                }
                fallbackServers.add(server.getValue());
            }
        }
        if(fallbackServers.size() == 0) {
            return null;
        } else {
            return fallbackServers.get(new Random().nextInt(fallbackServers.size()));
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getBungeePerms(String uuid) {
        MongoCollection<Document> permCollection = BungeePlugin.getPlugin().getMongoManager().getCollection(MongoManager.PERMISSIONS);
        MongoCollection<Document> playerCollection = BungeePlugin.getPlugin().getMongoManager().getCollection(MongoManager.BLOODPLAYER);
        Document playerDoc = playerCollection.find(new Document("UUID", uuid)).first();

        ArrayList<String> bungeePerms = new ArrayList<>();

        if(playerDoc != null) {
            ArrayList<String> permGroups = (ArrayList<String>) playerDoc.get("PERM_GROUP");

            if (permGroups != null) {
                permGroups.forEach(group ->
                    bungeePerms.addAll((ArrayList<String>)permCollection.find(Filters.eq("GroupName", group)).first().get("Bungee_Perms"))
                );
            }
        }

        return bungeePerms;
    }

    public String toTimeString(long millis, String daySuffix, String hourSuffix, String minuteSuffix, String secondSuffix) {

        if(millis == -1)
            return "PERMANENT";

        int days = 0, hours = 0, minutes = 0, seconds = 0;

        final int dayMillis = 1000 * 60 * 60 * 24;
        final int hourMillis = dayMillis / 24;


        while (millis >= dayMillis) {
            days++;
            millis -= dayMillis;
        }
        while (millis >= hourMillis) {
            hours++;
            millis -= hourMillis;
        }
        while (millis >= hourMillis / 60) {
            minutes++;
            millis -= hourMillis / 60;
        }
        while (millis >= hourMillis / 60 / 60) {
            seconds++;
            millis -= hourMillis / 60 / 60;
        }

        String timeString = "";
        if(days == 0) {
            timeString = timeString.concat(seconds + secondSuffix);
        }

        timeString = hours + hourSuffix + " " + minutes + minuteSuffix + " " + timeString;
        if(days != 0){
            timeString = days + daySuffix + " " + timeString;
        }

        return timeString;
    }
}
