package de.alb654.AlbyCloud.commands.commandMangers;

import de.alb654.AlbyCloud.commands.CommandManager;
import de.alb654.AlbyCloud.commands.commands.cloud.*;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public class CloudCommandManager extends CommandManager {

    @Override
    protected void addCommands() {
        addCommand(new StopCommand());
        addCommand(new StartServerCommand());
        addCommand(new StopServerCommand());
        addCommand(new GroupCommand());
        addCommand(new ListServerCommand());
        addCommand(new ServerInfoCommand());
    }
}
