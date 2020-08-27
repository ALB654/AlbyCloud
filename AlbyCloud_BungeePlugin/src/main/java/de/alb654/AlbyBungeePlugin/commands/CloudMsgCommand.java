package de.alb654.AlbyBungeePlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 9.7.2020
 */

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class CloudMsgCommand extends MsgCommand {

    public CloudMsgCommand(String name) {
        super(name, "BloodMC.CloudUpdates", "cloudmsg");
    }
}
