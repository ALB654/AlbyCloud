package de.alb654.AlbyCloud.core.groups;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 25.6.2020
 */

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.screens.LogLevel;
import de.alb654.AlbyCloud.screens.Screen;

import java.util.ArrayList;

public abstract class Group<T extends Server> {

    private final String name;
    private final String serverVersion;
    private final Server.Type serverType;
    private final int ramPerServer;

    private boolean enabled;

    private final Screen cloudScreen;
    private final ArrayList<T> serverList = new ArrayList<>();

    protected Group(String name, String serverVersion, Server.Type serverType, int ramPerServer, Screen cloudScreen) {
        this.name = name;
        this.serverVersion = serverVersion;
        this.serverType = serverType;
        this.ramPerServer = ramPerServer;
        this.cloudScreen = cloudScreen;

        this.enabled = true;
    }

    public abstract void checkServers();

    public void checkIfServersAreRunning() {
        ArrayList<T> stopped = new ArrayList<>();
        for(T server : serverList) {
            if(server.isStopped())
                stopped.add(server);
        }
        serverList.remove(stopped);
    }

    protected void startOneServer() {
        startOneServer(name);
    }

    @SuppressWarnings("unchecked")
    protected void startOneServer(String id) {
        cloudScreen.addMsg("Start Server in Gruppe '" + name + "'");
        T server = (T) Main.getAlbyCloud().getServerManager().startServer(serverType, id, serverVersion, ramPerServer);

        if(server != null) {
            server.registerAsGroupServer();
            serverList.add(server);
        } else {
            enabled = false;
            cloudScreen.addMsg("Gruppe '" + name + "' konnte keine Server starten und hat sich deaktiviert", LogLevel.WARNING);
        }
    }

    protected void stopOneServer() {
        T server = null;
        for(T runnings : serverList) {
            if(runnings.getOnlinePlayers() == 0) {
                server = runnings;
            }
        }

        cloudScreen.addMsg("Stoppe Server in Gruppe '" + name + "'");
        server.stop();
        serverList.remove(server);
    }

    public void stopAllServer() {
        cloudScreen.addMsg("Stoppe alle Server aus Gruppe '" + name + "'...");
        serverList.forEach(Server::stop);
    }

    public ArrayList<T> listServers() {
        return serverList;
    }

    void disable(){
        this.enabled = false;
    }

    void enable() {
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int onlineServerAmount() {
        return serverList.size();
    }

    public String getName() {
        return name;
    }

    public Server.Type getServerType() {
        return serverType;
    }
}
