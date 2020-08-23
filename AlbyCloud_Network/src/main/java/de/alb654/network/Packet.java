package de.alb654.network;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 20.6.2020
 */

import java.io.Serializable;

public abstract class Packet implements Serializable {

    protected static final long serialVersionUID = 1L;

    private final String packetID;

    public Packet(String packetID) {
        this.packetID = packetID;
    }

    public String getPacketID() {
        return packetID;
    }
}
