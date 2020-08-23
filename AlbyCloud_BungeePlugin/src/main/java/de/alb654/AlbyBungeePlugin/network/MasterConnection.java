package de.alb654.AlbyBungeePlugin.network;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 20.6.2020
 */

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.network.handle.PacketHandler;
import de.alb654.AlbyBungeePlugin.network.handle.handler.master.*;
import de.alb654.network.Packet;
import de.alb654.network.bungee.BungeeInLoginPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class MasterConnection {

    private Socket socket;
    private final String host;
    private final int port;

    private final HashMap<String, PacketHandler<?>> handler = new HashMap<>();

    private boolean listen;

    {
        addPacketHandler("BUNGEE_SHUTDOWN", packet -> {
            System.out.println(BungeePlugin.Cprefix + "Shutdown von Master angefordert... Stoppe");

            for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.disconnect(new ComponentBuilder("§7• §b§lBloodMC §7•\n\n")
                        .append("§4KEINE ANGST WIR SIND NICHT WEG!\n\nDer Server startet neu!\nBitte joine in wenigen Sekunden erneut.")
                        .color(ChatColor.RED).create());
            }

            BungeePlugin.getPlugin().getProxy().stop();
        });

        addPacketHandler("BUNGEE_ADD_SERVER", new BungeeAddServerHandler());
        addPacketHandler("BUNGEE_REMOVE_SERVER", new BungeeRemoveServerHandler());
        addPacketHandler("BUNGEE_COMMAND", new BungeeCommandHandler());
        addPacketHandler("BUNGEE_PLAYER_MESSAGE", new BungeePlayerMassageHandler());
        addPacketHandler("BUNGEE_CLOUD_MESSAGE", new BungeeCloudMessageHandler());
        addPacketHandler("BUNGEE_MOVE_PLAYERS", new BungeeMovePlayersHandler());
    }

    public MasterConnection(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            connect(host, port);
            System.out.println(BungeePlugin.Cprefix + "Verbindung zum Master aufgebaut!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(new BungeeInLoginPacket());
        oos.flush();
    }

    public void startListeningForPackets() {
        listen = true;
        new Thread(() -> {
            try {
                System.out.println(BungeePlugin.Cprefix + "Starte listening von Packets");
                while(listen) {
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Object o = ois.readObject();

                    if(o instanceof Packet) {
                        Packet p = (Packet) o;
                        handlePacket(p);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }, "Packet-Listening-Thread").start();
    }

    public void handlePacket(Packet packet) {
        PacketHandler packetHandler = handler.get(packet.getPacketID());
        if(packetHandler != null)
            packetHandler.handle(packet);
        else
            System.out.println(BungeePlugin.Cprefix + "Konnte Packet '" + packet.getPacketID() + "' nicht handeln");
    }

    private void addPacketHandler(String packetID, PacketHandler handler) {
        this.handler.put(packetID, handler);
    }

    public void send(Packet p) {
        try {
            Socket temp = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(temp.getOutputStream());
            oos.writeObject(p);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopListening() {
        listen = false;
    }

}
