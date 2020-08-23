package de.alb654.AlbySpigotPlugin.api.server;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 28.6.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.network.spigot.SpigotInUpdatePacket;
import org.bukkit.scheduler.BukkitRunnable;

public class CloudServer {

    private final String id;
    private String motd;

    private int onlinePlayer;
    private int maxPlayer;

    private State state;

    private boolean pendingUpdate;

    public CloudServer(String id, String motd, int onlinePlayer, int maxPlayer, State state) {
        this.id = id;
        this.motd = motd;
        this.onlinePlayer = onlinePlayer;
        this.maxPlayer = maxPlayer;
        this.state = state;

        this.pendingUpdate = false;
    }

    public void updateStatsToMaster() {
        if(!pendingUpdate) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    SpigotPlugin.getSpigotPlugin().getMasterConnection().send(
                            new SpigotInUpdatePacket(id, motd, onlinePlayer, maxPlayer, state.id));
                    pendingUpdate = false;
                }
            }.runTaskLater(SpigotPlugin.getSpigotPlugin(), 20 * 5);
        }
    }

    public CloudServer setMotd(String motd) {
        this.motd = motd;
        return this;
    }

    public CloudServer setOnlinePlayer(int onlinePlayer) {
        this.onlinePlayer = onlinePlayer;
        return this;
    }

    public CloudServer addOnePlayer() {
        setOnlinePlayer(getOnlinePlayer() + 1);
        return this;
    }

    public CloudServer removeOnePlayer() {
        setOnlinePlayer(Math.max(getOnlinePlayer() - 1, 0));
        return this;
    }

    public CloudServer setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
        return this;
    }

    public CloudServer setState(State state) {
        this.state = state;
        return this;
    }

    public int getOnlinePlayer() {
        return onlinePlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public String getMotd() {
        return motd;
    }

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public enum State {
        LOADING(0), LOBBY(1), INGAME(2), ENDING(3), STATIC(4);

        int id;
        State(int id) {
            this.id = id;
        }

        public static State fromId(int id) {
            for(State s : values()) {
                if(s.id == id)
                    return s;
            }
            return null;
        }
    }
}
