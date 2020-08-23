package de.alb654.AlbyCloud.commands.commands.bungee;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.network.bungee.BungeeOutCommandPacket;

public class BungeeCommandCommand extends Command {


    public BungeeCommandCommand() {
        super("cmd", "Führt einen Command aus", "cmd <cmd> <args...>", "exe", "command", "execute", "exc");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        Main.getAlbyCloud().getCloudNetworkServer().sendPacket("Bungee", new BungeeOutCommandPacket(args));
    }
}
