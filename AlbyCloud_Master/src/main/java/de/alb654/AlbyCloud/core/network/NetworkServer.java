package de.alb654.AlbyCloud.core.network;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 20.6.2020
 */

import de.alb654.AlbyCloud.core.network.handle.PacketHandler;
import de.alb654.AlbyCloud.screens.LogLevel;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.network.Packet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public abstract class NetworkServer {

    private ServerSocket serverSocket;
    private final HashMap<String, Socket> clients = new HashMap<>();
    private final HashMap<String, PacketHandler> packetHandler = new HashMap<>();

    private final int port;
    private boolean shutdown;

    private Screen cloudScreen;

    public NetworkServer(int port) {
        this.port = port;
        this.shutdown = false;
    }

    public void connect() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            cloudScreen.addError(e, "starten des Network-Servers (Port: " + port + ")");
        }
    }

    public void startListneningToClients() {
        new Thread(() -> {
            try {

                while(!shutdown) {

                    Socket socket = serverSocket.accept();

                    if(!socket.getInetAddress().getHostAddress().equals(serverSocket.getInetAddress().getHostAddress())) {
                        if(!socket.getInetAddress().getHostAddress().equals("0.0.0.0") && !socket.getInetAddress().getHostAddress().equals("127.0.0.1"))
                            socket.close();
                    }

                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Object obj = ois.readObject();

                    if (obj instanceof Packet) {
                        Packet p = (Packet) obj;
                        handlePacket(p, socket);
                    } else {
                        socket.close();
                    }

                }

            } catch (IOException | ClassNotFoundException e) {
                cloudScreen.addError(e, "empfangen eines Packets (Port: " + port + ")");
            }
        }, "Network-Listen-Thread").start();
    }

    private void handlePacket(Packet p, Socket socket) {
        PacketHandler handler = packetHandler.get(p.getPacketID());
        if(handler != null) {
            cloudScreen.addMsg("Packet '" + p.getPacketID() + "' von " + socket.getInetAddress() + ":" + socket.getPort() + " empfangen", LogLevel.DEBUG);
            handler.handle(p, socket);
        } else
            cloudScreen.addMsg("Packet '" + p.getPacketID() + "' auf Port " + port + " empfangen, aber es existiert kein passender Handler", LogLevel.WARNING);
    }

    public void sendPacket(String identifier, Packet p) {
        if(!shutdown) try {
            Socket server = clients.get(identifier.toUpperCase());
            if(server == null || server.isClosed()) {
                return;
            }

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(p);
            oos.flush();
        } catch (IOException e) {
            cloudScreen.addError(e, "senden eines Packets (" + p.getPacketID() + ") zu " + identifier.toUpperCase());
        }
    }

    public void sendToAll(Packet p) {
        clients.forEach((i, s) -> {
            if(!i.equals("Bungee")) sendPacket(i, p);
        });
    }

    public void stop() {
        this.shutdown = true;
    }

    protected void addClient(String identifier, Socket socket) {
        clients.put(identifier.toUpperCase(), socket);
    }

    public void removeClient(String identifier) {
        clients.remove(identifier);
    }

    protected void addPacketHandler(String packetId, PacketHandler handler) {
        packetHandler.put(packetId, handler);
    }

    public void setCloudScreen(Screen cloudScreen) {
        this.cloudScreen = cloudScreen;
    }

}
