package de.alb654.AlbyCloud.commands;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 27.04.2020.
 */
@FunctionalInterface
public interface CommandCallback {

    void done(boolean succcess, String msg);

}
