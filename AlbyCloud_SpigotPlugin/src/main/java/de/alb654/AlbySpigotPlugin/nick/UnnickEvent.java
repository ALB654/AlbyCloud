package de.alb654.AlbySpigotPlugin.nick;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 1.8.2020
 */


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UnnickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public UnnickEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
