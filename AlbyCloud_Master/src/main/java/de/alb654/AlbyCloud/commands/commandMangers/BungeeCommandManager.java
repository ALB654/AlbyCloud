package de.alb654.AlbyCloud.commands.commandMangers;

import de.alb654.AlbyCloud.commands.CommandManager;
import de.alb654.AlbyCloud.commands.commands.bungee.BungeeCommandCommand;


/*
 * Copyright (c)
 * Created by Aaron Bernert on 19.6.2020
 */
public class BungeeCommandManager extends CommandManager {
    @Override
    protected void addCommands() {
        addCommand(new BungeeCommandCommand());
    }
}
