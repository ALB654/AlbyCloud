package de.alb654.AlbyCloud.commands;

import de.alb654.AlbyCloud.commands.commands.HelpCommand;
import de.alb654.AlbyCloud.commands.commands.SwitchScreenCommand;

import java.util.ArrayList;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public abstract class CommandManager {

    private ArrayList<Command> commands;

    {
        commands = new ArrayList<>();

        //Standard Commands

        addCommand(new HelpCommand());
        addCommand(new SwitchScreenCommand());

        //Add other Commands

        addCommands();
    }

    protected abstract void addCommands();

    protected void addCommand(Command cmd) {
        this.commands.add(cmd);
    }

    public void executeCommand(String name, String[] args, CommandCallback callback) {
        getCommand(name).execute(args, callback);
    }

    public Command getCommand(String name) {
        for(Command cmd : commands)
            if(cmd.getName().equalsIgnoreCase(name))
                return cmd;
            else for(String alias : cmd.getAliases())
                if(alias.equalsIgnoreCase(name))
                    return cmd;
        return new EmptyCommand();
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public class EmptyCommand extends Command {

        public EmptyCommand() {
            super("EmptyCommand", "Command nicht gefunden", "Command nicht gefunden");
        }

        @Override
        public void execute(String[] args, CommandCallback callback) {
            callback.done(false, "Command nicht gefunden");
        }
    }
}
