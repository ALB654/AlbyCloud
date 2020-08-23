package de.alb654.AlbyCloud.core.groups;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.core.server.dynamic.DynamicServer;
import de.alb654.AlbyCloud.screens.Screen;

public class DynamicGroup extends Group<DynamicServer> {

    private final int standardAmountOfServer;
    private final int maxAmountOfServer;
    private final int onlinePlayerPercentageUntilNewServerStart;

    protected DynamicGroup(String name, String serverVersion, int ramPerServer, int standardAmountOfServer, int maxAmountOfServer, int onlinePlayerPercentageUntilNewServerStart, Screen cloudScreen) {
        super(name, serverVersion, Server.Type.DYNAMIC, ramPerServer, cloudScreen);
        this.standardAmountOfServer = standardAmountOfServer;
        this.maxAmountOfServer = maxAmountOfServer;
        this.onlinePlayerPercentageUntilNewServerStart = onlinePlayerPercentageUntilNewServerStart;
    }

    @Override
    public void checkServers() {
        if(standardAmountOfServer > onlineServerAmount()) {
            startOneServer();
        } else {

            if(standardAmountOfServer < onlineServerAmount() && amountOfEmptyServer() > 0) {
                stopOneServer();
            } else if(maxAmountOfServer < onlineServerAmount() && allServerFullyOccupied()) {
                startOneServer();
            }
        }
    }

    private int amountOfEmptyServer() {
        int amount = 0;
        for(DynamicServer server : listServers()) {
            if(server.getOnlinePlayers() == 0)
                amount++;
        }
        return amount;
    }

    private boolean allServerFullyOccupied() {
        for(DynamicServer server : listServers()) {
            if(server.getPercentageOfPlayer() < onlinePlayerPercentageUntilNewServerStart)
                return false;
        }
        return true;
    }
}
