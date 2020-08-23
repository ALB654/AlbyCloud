package de.alb654.AlbyCloud.commands.commands.cloud;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.AlbyCloud.core.server.Server;

public class StartServerCommand extends Command {

    public StartServerCommand() {
        super("StartServer", "Startet einen Server", "StartServer <type> [...]","start");
    }

    /*

        StartServer static <id> <ram>
        StartServer dynamic <template> <ram>
        StartServer lobby <ram>

     */

    @Override
    public void execute(String[] args, CommandCallback callback) {
        if(args.length < 1) {
            callback.done(false, "StartServer <static/dynamic/lobby> [...]");
        } else {
            if(args[0].equalsIgnoreCase("lobby")) {
                if(args.length >= 2) {

                    int ram;
                    try {
                        ram = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        callback.done(false, "StartServer lobby <ram> [version]");
                        return;
                    }

                    String version = null;
                    if(args.length >= 3){
                        version = args[2];
                    }

                    Main.getAlbyCloud().getServerManager().startServer(Server.Type.LOBBY, "LOBBY", version, ram);
                } else {
                    callback.done(false, "StartServer lobby <ram> [version]");
                }

            } else if(args[0].equalsIgnoreCase("dynamic")) {
                if(args.length >= 3) {
                    String template = args[1].toUpperCase();

                    int ram;
                    try {
                        ram = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        callback.done(false, "StartServer dynamic <template> <ram> [version]");
                        return;
                    }

                    String version = null;
                    if(args.length >= 4){
                        version = args[2];
                    }

                    Main.getAlbyCloud().getServerManager().startServer(Server.Type.DYNAMIC, template, version, ram);
                } else {
                    callback.done(false, "StartServer dynamic <template> <ram> [version]");
                }

            } else if(args[0].equalsIgnoreCase("static")) {
                if(args.length >= 3) {
                    String id = args[1].toUpperCase();

                    int ram;
                    try {
                        ram = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        callback.done(false, "StartServer static <id> <ram> [version]");
                        return;
                    }

                    String version = null;
                    if(args.length >= 4){
                        version = args[2];
                    }

                    Main.getAlbyCloud().getServerManager().startServer(Server.Type.STATIC, id, version, ram);
                } else {
                    callback.done(false, "StartServer static <id> <ram> [version]");
                }
            } else {
                callback.done(false, "StartServer <static/dynamic/lobby> [...]");
            }
        }
    }
}
