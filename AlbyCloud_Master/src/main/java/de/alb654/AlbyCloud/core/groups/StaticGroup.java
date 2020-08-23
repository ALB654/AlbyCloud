package de.alb654.AlbyCloud.core.groups;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.core.server.statics.StaticServer;
import de.alb654.AlbyCloud.screens.Screen;

public class StaticGroup extends Group<StaticServer> {

    private final int amountOfServer;

    protected StaticGroup(String name, String serverVersion, int ramPerServer, Screen cloudScreen, int amountOfServer) {
        super(name, serverVersion, Server.Type.STATIC, ramPerServer, cloudScreen);
        this.amountOfServer = amountOfServer;
    }

    @Override
    public void checkServers() {
        if(amountOfServer < onlineServerAmount())
            stopOneServer();
        else if(amountOfServer > onlineServerAmount())
            startOneServer(getName() + (onlineServerAmount() + 1));
    }
}
