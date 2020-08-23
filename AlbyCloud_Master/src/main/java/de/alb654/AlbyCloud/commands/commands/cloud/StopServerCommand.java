package de.alb654.AlbyCloud.commands.commands.cloud;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 22.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.AlbyCloud.core.server.Server;

public class StopServerCommand extends Command {

    public StopServerCommand() {
        super("StopServer", "Stoppt einen Server", "StopServer <ID>", "StopS", "delS", "removeServer", "remS", "deleteServer");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        if(args.length < 1) {
            callback.done(false, "Es muss ein Server angegeben werden!");
            return;
        }
        Server s = Main.getAlbyCloud().getServerManager().getServer(args[0]);
        if(s == null) {
            callback.done(false, "Server '" + args[0] + "' ist nicht online!");
            return;
        }
        s.stop();

    }
}
