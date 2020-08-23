package de.alb654.AlbyCloud.commands;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public abstract class Command {

    private String name;
    private String[] aliases;
    private String description;
    private String usage;

    public Command(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    public abstract void execute(String[] args, CommandCallback callback);

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }
}
