package de.alb654.AlbyCloud.commands.commandMangers;

import de.alb654.AlbyCloud.commands.CommandManager;

import java.util.ArrayList;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public class CommandManagerCollection {

    private ArrayList<CommandManager> managers = new ArrayList<>();

    {
        addManager(new CloudCommandManager());
        addManager(new ServerCommandManager());
        addManager(new BungeeCommandManager());
    }

    private void addManager(CommandManager man) {
        managers.add(man);
    }


    public <T extends CommandManager> T getCommandManager(Class<T> clazz) {
        for(CommandManager cm : managers) {
            if(clazz.isInstance(cm))
                return (T) cm;
        }
        return null;
    }
}
