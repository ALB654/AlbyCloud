package de.alb654.network.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.network.Packet;

import java.net.InetSocketAddress;

public class BungeeOutAddServerPacket extends Packet {

    private String serverID;
    private InetSocketAddress address;
    private String motd;

    public BungeeOutAddServerPacket(String serverID, InetSocketAddress address, String motd) {
        super("BUNGEE_ADD_SERVER");
        this.serverID = serverID;
        this.address = address;
        this.motd = motd;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getMotd() {
        return motd;
    }

    public String getServerID() {
        return serverID;
    }
}
