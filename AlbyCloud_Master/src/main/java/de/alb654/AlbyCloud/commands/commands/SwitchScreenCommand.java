package de.alb654.AlbyCloud.commands.commands;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.Command;
import de.alb654.AlbyCloud.commands.CommandCallback;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.screens.ScreenManager;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 28.04.2020.
 */
public class SwitchScreenCommand extends Command {

    public SwitchScreenCommand() {
        super("SwitchScreen", "Wechselt zwischen den Screen", "switchScreen <name>", "switch", "ss", "screen");
    }

    @Override
    public void execute(String[] args, CommandCallback callback) {
        if(args.length == 0) {
            callback.done(false, "Kein Screen angegeben");
            return;
        }

        ScreenManager manager = Main.getAlbyCloud().getScreenManager();
        Screen s;

        if(args[0].equalsIgnoreCase("error")) {
            s = manager.getScreen(ScreenManager.ERROR_SCREEN);
        } else {
            s = manager.getScreen(args[0]);

            if(s.getName().equals(ScreenManager.ERROR_SCREEN)) {
                callback.done(false, "Der Screen '" + args[0] + "' existiert nicht");
                return;
            }
        }

        manager.activateScreen(s.getName());
        callback.done(true, "Screen gewechselt!");
    }
}
