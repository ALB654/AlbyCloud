package de.alb654.AlbyCloud.commands.commands.cloud;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 29.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.screens.ScreenManager;

public class ServerInfoCommand extends Command {

    public ServerInfoCommand() {
        super("ServerInfo", "Zeigt Informationen über einen Server an", "ServerInfo <Server>", "sinfo");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        if(args.length != 1) {
            callback.done(false, "ServerInfo <Server>");
            return;
        }
        Server server = Main.getAlbyCloud().getServerManager().getServer(args[0]);
        if(server == null) {
            callback.done(false, "Server '" + args[0] + "' existiert nicht");
            return;
        }

        Screen screen = Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN);

        screen.addMsg("-------- " + server.getId() + " --------");
        screen.addMsg("Player: " + server.getOnlinePlayers() + "/" + server.getMaxPlayers());
        screen.addMsg("State: " + server.getServerState().name());
        screen.addMsg("MOTD: " + server.getMotd());
        screen.addMsg("Type: " + server.getServerType().name());
        screen.addMsg("----------------------------------------");
    }
}
