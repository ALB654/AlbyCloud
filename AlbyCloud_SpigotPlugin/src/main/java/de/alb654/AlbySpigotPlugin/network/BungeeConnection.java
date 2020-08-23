package de.alb654.AlbySpigotPlugin.network;

import com.google.common.collect.Iterables;
import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.6.2020
 */

public class BungeeConnection implements PluginMessageListener {

    {
        SpigotPlugin.getSpigotPlugin().getServer().getMessenger().registerOutgoingPluginChannel(SpigotPlugin.getSpigotPlugin(), "BungeeCord");
        SpigotPlugin.getSpigotPlugin().getServer().getMessenger().registerIncomingPluginChannel(SpigotPlugin.getSpigotPlugin(), "BungeeCord", this);
    }

    public void sendMessage(Player player, String channel, String... args) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF(channel);
            out.writeUTF("A*'I:" + args.length);
            for(String arg : args)
                out.writeUTF(arg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(SpigotPlugin.getSpigotPlugin(), "BungeeCord", b.toByteArray());
    }

    public void sendMessage(String channel, String... args) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF(channel);
            out.writeUTF("A*'I:" + args.length);
            for(String arg : args)
                out.writeUTF(arg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if(player != null)
            player.sendPluginMessage(SpigotPlugin.getSpigotPlugin(), "BungeeCord", b.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }
}
