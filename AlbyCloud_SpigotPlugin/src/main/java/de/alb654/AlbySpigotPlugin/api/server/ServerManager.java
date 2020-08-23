package de.alb654.AlbySpigotPlugin.api.server;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 29.6.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.network.spigot.SpigotInMovePlayersPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLightningStrike;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    private final ArrayList<CloudServer> servers = new ArrayList<>();

    public void addCloudServer(CloudServer server) {
        servers.add(server);
    }

    public void removeServer(CloudServer server) {
        servers.remove(server);
    }

    public CloudServer getThisServer() {
        return getServer(SpigotPlugin.getSpigotPlugin().getServerID());
    }

    public CloudServer getServer(String id) {
        for(CloudServer cs : servers) {
            if(cs.getId().equalsIgnoreCase(id))
                return cs;
        }
        return null;
    }

    public CloudServer getNextFreeLobby() {
        for(CloudServer server : servers) {
            if(server.getId().startsWith("LOBBY") && server.getMaxPlayer() > server.getOnlinePlayer()) {
                return server;
            }
        }
        return null;
    }

    public void sendPlayersToServer(List<Player> players, String serverId) {
        if(SpigotPlugin.getSpigotPlugin().isEnabled())
            for(Player player : players)
                CloudApi.getInstance().getBungeeConnection().sendMessage(player, "sendPlayer", player.getUniqueId().toString(), serverId);
        else {
            String[] playerUuids = new String[players.size()];
            for(int i = 0; i < players.size(); i++) {
                playerUuids[i] = players.get(i).getUniqueId().toString();
                System.out.println(playerUuids[i]);
            }
            System.out.println("ServerId: " + serverId);
            SpigotPlugin.getSpigotPlugin().getMasterConnection().send(new SpigotInMovePlayersPacket(playerUuids, serverId));
        }
    }

    public void sendPlayerToServer(Player player, String serverId) {
        if(SpigotPlugin.getSpigotPlugin().isEnabled())
            CloudApi.getInstance().getBungeeConnection().sendMessage(player, "sendPlayer", player.getUniqueId().toString(), serverId);
        else
            SpigotPlugin.getSpigotPlugin().getMasterConnection().send(new SpigotInMovePlayersPacket(new String[] { player.getUniqueId().toString() }, serverId));
    }

}
