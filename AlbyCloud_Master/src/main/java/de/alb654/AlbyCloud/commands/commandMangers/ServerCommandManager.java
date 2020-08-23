package de.alb654.AlbyCloud.commands.commandMangers;

import de.alb654.AlbyCloud.commands.CommandManager;
import de.alb654.AlbyCloud.commands.commands.spigot.SpigotCommandCommand;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 11.05.2020.
 */
public class ServerCommandManager extends CommandManager {

    @Override
    protected void addCommands() {
        addCommand(new SpigotCommandCommand());
    }
}
