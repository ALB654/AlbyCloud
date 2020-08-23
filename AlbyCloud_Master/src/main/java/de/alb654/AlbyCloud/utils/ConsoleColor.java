package de.alb654.AlbyCloud.utils;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 14.10.2019.
 */
public enum ConsoleColor {

    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[37m");

    private String id;

    ConsoleColor(String id) {
        this.id = id;
    }

    public String Id() {
        return id;
    }
}
