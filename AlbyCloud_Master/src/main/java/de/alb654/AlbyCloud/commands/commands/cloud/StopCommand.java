package de.alb654.AlbyCloud.commands.commands.cloud;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public class StopCommand extends Command {

    public StopCommand() {
        super("stopCloud", "Stoppt die cloud", "stopcloud", "stop", "exit", "quit", "end");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        if(args.length != 0) {
            callback.done(false, "Es wurden Argumente angegeben!");
            return;
        }
        new Thread(() -> Main.getAlbyCloud().stop(), "Cloud-Shutdown-Thread").start();
    }
}
