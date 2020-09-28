package de.alb654.network.spigot;/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.9.2020
 */

import de.alb654.network.Packet;

public class SpigotInCloudCommandPacket extends Packet {

    private final String command;
    private final String[] args;
    private final String playerUUID;

    public SpigotInCloudCommandPacket(String command, String[] args, String playerUUID) {
        super("SPIGOT_CLOUD_COMMAND");
        this.command = command;
        this.args = args;
        this.playerUUID = playerUUID;
    }

    public String[] getArgs() {
        return args;
    }

    public String getCommand() {
        return command;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }
}
