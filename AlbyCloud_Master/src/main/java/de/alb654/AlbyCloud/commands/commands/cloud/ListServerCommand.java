package de.alb654.AlbyCloud.commands.commands.cloud;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 29.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.AlbyCloud.core.groups.Group;
import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.screens.ScreenManager;

public class ListServerCommand extends Command {
    public ListServerCommand() {
        super("listServer", "Zeigt alle aktiven Server an", "listServer", "list", "servers");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        Screen screen = Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN);

        screen.addMsg("-------- SERVER --------");
        screen.addMsg("Gruppen:");

        for(Group<?> group : Main.getAlbyCloud().getGroupManager().getGroups()) {
            screen.addMsg(" > " + group.getName() + " (" + group.getServerType().name() + "):");

            for(Server groupServer : group.listServers()) {
                screen.addMsg("    > " + groupServer.getId());
            }
            screen.addMsg("  ");
        }

        screen.addMsg("Einzelne Server:");

        screen.addMsg(" > Lobbys:");
        for(Server s : Main.getAlbyCloud().getServerManager().getLobbyServers()) {
            screen.addMsg("   > " + s.getId());
        }

        screen.addMsg(" > Dynamic:");
        for(Server s : Main.getAlbyCloud().getServerManager().getDynamicServers()) {
            screen.addMsg("   > " + s.getId());
        }

        screen.addMsg(" > Static:");
        for(Server s : Main.getAlbyCloud().getServerManager().getStaticServers()) {
            screen.addMsg("   > " + s.getId());
        }


        screen.addMsg("------------------------");
    }
}
