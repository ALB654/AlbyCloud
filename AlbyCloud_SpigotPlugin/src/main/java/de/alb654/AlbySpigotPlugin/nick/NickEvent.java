package de.alb654.AlbySpigotPlugin.nick;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 1.8.2020
 */

public class NickEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private String nickName;

    private boolean cancelled;

    public NickEvent(Player player, String nickName) {
        this.player = player;
        this.nickName = nickName;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
