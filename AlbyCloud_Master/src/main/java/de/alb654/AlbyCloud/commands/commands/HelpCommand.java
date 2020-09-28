package de.alb654.AlbyCloud.commands.commands;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.AlbyCloud.commands.CommandManager;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.utils.ConsoleColor;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public class HelpCommand extends Command {

    public HelpCommand() {
        super("Help", "Zeigt alle Commands an", "help [command]","hilfe");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        Screen s = Main.getAlbyCloud().getScreenManager().getActiveScreen();

        if(args.length == 0) {
            s.addMsg("========== " + ConsoleColor.ANSI_CYAN.Id() + "HILFE " + ConsoleColor.ANSI_RESET.Id() + "==========");
            s.addMsg("Commands:");

            for (Command c : Main.getAlbyCloud().getScreenManager().getActiveScreen().getCommandManager().getCommands()) {
                s.addMsg("  " + c.getName() + " | " + c.getDescription());
            }

            s.addMsg("===========================");
        } else if(args.length == 1) {
            Command c = Main.getAlbyCloud().getScreenManager().getActiveScreen().getCommandManager().getCommand(args[0]);
            if(c instanceof CommandManager.EmptyCommand) {
                callback.done(false, "Command '" + args[0] + "' nicht gefunden");
                return;
            }

            s.addMsg("========== " + ConsoleColor.ANSI_CYAN.Id() + c.getName() + " " + ConsoleColor.ANSI_RESET.Id() + "==========");
            s.addMsg("  >" + c.getName() + " | " + c.getDescription());
            s.addMsg("    Usage > " + c.getUsage());
            s.addMsg("      Aliases:");
            for (String a : c.getAliases()) {
                s.addMsg("           - " + a);
            }
            s.addMsg("===========================");
        }
    }
}
