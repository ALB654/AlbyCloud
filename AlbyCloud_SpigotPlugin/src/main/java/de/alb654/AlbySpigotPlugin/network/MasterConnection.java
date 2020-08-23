package de.alb654.AlbySpigotPlugin.network;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.network.handle.PacketHandler;
import de.alb654.AlbySpigotPlugin.network.handle.handler.SpigotCommandHandler;
import de.alb654.AlbySpigotPlugin.network.handle.handler.SpigotUpdateHandler;
import de.alb654.network.Packet;
import de.alb654.network.spigot.SpigotInLoginPacket;
import org.bukkit.Bukkit;

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
        addPacketHandler("SPIGOT_SHUTDOWN", packet -> {
            SpigotPlugin.sendMessage("Shutdown von Master angefordert... Stoppe");
            Bukkit.shutdown();
        });

        addPacketHandler("SPIGOT_COMMAND", new SpigotCommandHandler());
        addPacketHandler("SPIGOT_UPDATE", new SpigotUpdateHandler());
    }

    public MasterConnection(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            connect(host, port);
            SpigotPlugin.sendMessage("Verbindung zum Master aufgebaut!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(new SpigotInLoginPacket(SpigotPlugin.getSpigotPlugin().getServerID()));
        oos.flush();
    }

    public void startListeningForPackets() {
        listen = true;
        new Thread(() -> {
            try {
                SpigotPlugin.sendMessage("Starte listening von Packets");
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
            SpigotPlugin.sendMessage("Konnte Packet '" + packet.getPacketID() + "' nicht handeln");
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
