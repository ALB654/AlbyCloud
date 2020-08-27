package de.alb654.AlbyBungeePlugin.network;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.6.2020
 */

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.network.handle.handler.spigot.BanKickHandler;
import de.alb654.AlbyBungeePlugin.network.handle.handler.spigot.ReportHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SpigotConnection implements Listener {

    private final HashMap<String, PluginMessageHandler> messageHandler = new HashMap<>();

    {
        messageHandler.put("sendPlayer", args -> ProxyServer.getInstance().getPlayer(UUID.fromString(args[0])).connect(ProxyServer.getInstance().getServerInfo(args[1])));
        messageHandler.put("banKick", new BanKickHandler());
        messageHandler.put("report", new ReportHandler(BungeePlugin.getPlugin().getReportManager()));
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if(e.getTag().equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                PluginMessageHandler handler = messageHandler.get(in.readUTF());
                if(handler != null) {
                    String[] args;
                    String identMsg = in.readUTF();
                    int msgNumber;
                    if (identMsg.startsWith("A*'I:")) {
                        msgNumber = Integer.parseInt(identMsg.split(":")[1]);
                        System.out.println(msgNumber);
                        args = new String[msgNumber];

                        for (int i = 0; i < msgNumber; i++) {
                            args[i] = in.readUTF();
                        }
                        handler.handleMessage(args);
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public interface PluginMessageHandler {
        void handleMessage(String[] args);
    }

}
