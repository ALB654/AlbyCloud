package de.alb654.AlbyCloud.commands.commands.cloud;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.AlbyCloud.core.groups.GroupManager;

public class GroupCommand extends Command {


    public GroupCommand() {
        super("group", "Erstelle/lösche/aktiviere/deaktiviere eine Gruppe", "group <create/delete/enable/disable> <name> [...]");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        if(args.length < 2) {
            callback.done(false, "group <create/delete/enable/disable> <name> [...]");
            return;
        }

        String name = args[1];
        GroupManager manager = Main.getAlbyCloud().getGroupManager();

        /*

        group create <name> static <amountOfServer> <ramPerServer>
        group create <name> dynamic <standardAmountOfServer> <ramPerServer> <maxAmountOfServer> <maxPercentPlayer>

         */
        if(args[0].equalsIgnoreCase("create")) {
            if(args.length < 3) {
                callback.done(false, "group create <name> static/dynamic [...]");
                return;
            }
            if(args[2].equalsIgnoreCase("static")) {
                if(args.length != 6) {
                    callback.done(false, "group create <name> static <amountOfServer> <ramPerServer> <version>");
                    return;
                }

                int amountOfServer;
                int ramPerServer;

                try {
                    amountOfServer = Integer.parseInt(args[3]);
                    ramPerServer = Integer.parseInt(args[4]);
                } catch (NumberFormatException e){
                    callback.done(false, "Bitte gebe Zahlen an!");
                    return;
                }
                manager.startStaticGroup(name, args[5], amountOfServer, ramPerServer);
            } else if(args[2].equalsIgnoreCase("dynamic")) {
                if(args.length != 8) {
                    callback.done(false, "group create <name> dynamic <standardAmountOfServer> <ramPerServer> <maxAmountOfServer> <maxPercentPlayer> <version>");
                    return;
                }

                int standardAmountOfServer;
                int ramPerServer;
                int maxAmountOfServer;
                int maxPercentPlayer;

                try {
                    standardAmountOfServer = Integer.parseInt(args[3]);
                    ramPerServer = Integer.parseInt(args[4]);
                    maxAmountOfServer = Integer.parseInt(args[5]);
                    maxPercentPlayer = Integer.parseInt(args[6]);
                } catch (NumberFormatException e){
                    callback.done(false, "Bitte gebe Zahlen an!");
                    return;
                }
                manager.startDynamicGroup(name, args[7], standardAmountOfServer, ramPerServer, maxAmountOfServer, maxPercentPlayer);
            }
        } else if(args[0].equalsIgnoreCase("delete")) {
            manager.deleteGroup(name);
        } else if(args[0].equalsIgnoreCase("enable")) {
            manager.activateGroup(name);
        } else if(args[0].equalsIgnoreCase("disable")) {
            manager.deactivateGroup(name);
        }
    }
}
