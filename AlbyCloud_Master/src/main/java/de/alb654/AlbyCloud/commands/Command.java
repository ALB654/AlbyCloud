package de.alb654.AlbyCloud.commands;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 02.11.2019.
 */
public abstract class Command {

    private final String name;
    private final String[] aliases;
    private final String description;
    private final String usage;

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
