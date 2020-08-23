package de.alb654.AlbyCloud.screens;

import de.alb654.AlbyCloud.utils.ConsoleColor;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 14.10.2019.
 */
public enum LogLevel {

    INFO("Info > ", ConsoleColor.ANSI_RESET),
    WARNING("[Warn] - ", ConsoleColor.ANSI_YELLOW),
    ERROR(">> ERROR << - ", ConsoleColor.ANSI_RED),
    INPUT("Input > ", ConsoleColor.ANSI_RESET),
    DEBUG("<DEBUG> ", ConsoleColor.ANSI_GREEN);

    private String prefix;
    private ConsoleColor cc;

    LogLevel(String prefix, ConsoleColor color) {
        this.prefix = prefix;
        this.cc = color;
    }

    public ConsoleColor getColor() {
        return cc;
    }

    public String getPrefix() {
        return prefix;
    }
}
