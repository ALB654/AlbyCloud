package de.alb654.AlbyCloud.core.groups;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.6.2020
 */

import de.alb654.AlbyCloud.screens.LogLevel;
import de.alb654.AlbyCloud.screens.Screen;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GroupManager {

    private final ArrayList<Group> groups = new ArrayList<>();

    private ScheduledExecutorService groupServerCheckService;
    private boolean isChecking = false;

    private final Screen cloudScreen;

    public GroupManager(Screen cloudScreen) {
        this.cloudScreen = cloudScreen;
    }

    public void startStaticGroup(String name, String version, int amountOfServer, int ramPerServer) {
        cloudScreen.addMsg("Starte neue StaticGroup '" + name + "'...");
        groups.add(new StaticGroup(name, version, ramPerServer, cloudScreen, amountOfServer));
        if(!isChecking)
            startServerChecking();
    }

    public void startDynamicGroup(String name, String version, int standardAmountOfServer, int ramPerServer, int maxAmountOfServer, int maxPercentPlayer) {
        cloudScreen.addMsg("Starte neue DynamicGroup '" + name + "'...");
        groups.add(new DynamicGroup(name, version, ramPerServer, standardAmountOfServer, maxAmountOfServer, maxPercentPlayer, cloudScreen));
        if(!isChecking)
            startServerChecking();
    }

    public void deaktivateAllGroups() {
        groups.forEach(Group::disable);
        stopServerChecking();
    }

    public void deleteGroup(String name) {
        cloudScreen.addMsg("Lösche Gruppe '" + name + "'");
        Group g = deactivateGroup(name);
        if(g != null) {
            g.stopAllServer();
            groups.remove(g);
        }
    }

    public void activateGroup(String name) {
        Group g = getGroup(name);
        if(g != null) {
            g.enable();
            if(!isChecking)
                startServerChecking();
            cloudScreen.addMsg("Gruppe '" + name + "' aktiviert!");
        } else {
            cloudScreen.addMsg("Gruppe '" + name + "' existiert nicht!", LogLevel.ERROR);
        }
    }

    public Group deactivateGroup(String name) {
        Group g = getGroup(name);
        if(g != null) {
            g.disable();
            cloudScreen.addMsg("Gruppe '" + name + "' deaktiviert!");
            return g;
        } else {
            cloudScreen.addMsg("Gruppe '" + name + "' existiert nicht!", LogLevel.ERROR);
            return null;
        }
    }

    private Group getGroup(String name) {
        for(Group group : groups) {
            if(group.getName().equalsIgnoreCase(name))
                return group;
        }
        return null;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    private void startServerChecking() {
        if(!isChecking) {
            isChecking = true;
            groupServerCheckService = Executors.newScheduledThreadPool(1);
            groupServerCheckService.scheduleAtFixedRate(() -> {
                groups.forEach(group -> {
                    group.checkIfServersAreRunning();
                    if(group.isEnabled())
                        group.checkServers();
                });

                if(noGroupsActive())
                    stopServerChecking();
            }, 1, 1, TimeUnit.SECONDS);
        }
    }

    private void stopServerChecking() {
        if(isChecking) {
            isChecking = false;
            groupServerCheckService.shutdownNow();
        }
    }

    private boolean noGroupsActive() {
        for(Group group : groups)
            if(group.isEnabled())
                return false;

        return true;
    }

}
