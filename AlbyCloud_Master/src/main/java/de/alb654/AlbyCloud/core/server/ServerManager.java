package de.alb654.AlbyCloud.core.server;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.core.RamManager;
import de.alb654.AlbyCloud.core.network.CloudNetworkServer;
import de.alb654.AlbyCloud.core.server.dynamic.DynamicServer;
import de.alb654.AlbyCloud.core.server.statics.StaticServer;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.file.FileManager;
import de.alb654.AlbyCloud.screens.LogLevel;
import de.alb654.AlbyCloud.screens.ScreenManager;
import de.alb654.AlbyCloud.utils.Waiter;
import de.alb654.network.bungee.BungeeOutRemoveServerPacket;
import de.alb654.network.spigot.SpigotOutUpdatePacket;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ServerManager {

    private static final int DEFAULT_MAX_PLAYERS = 8;

    private final ArrayList<DynamicServer> dynamicServers = new ArrayList<>();
    private final ArrayList<DynamicServer> lobbyServers = new ArrayList<>();
    private final ArrayList<StaticServer> staticServers = new ArrayList<>();

    private final ArrayList<Server> loadingServers = new ArrayList<>();

    private final ConfigManager configManager;
    private final FileManager fileManager;
    private final RamManager ramManager;

    public ServerManager(ConfigManager configManager, FileManager fileManager, RamManager ramManager){
        this.configManager = configManager;
        this.fileManager = fileManager;
        this.ramManager = ramManager;
    }

    public Server startServer(Server.Type type, String template, String version, int ram) {
        if(!ramManager.reserveRam(ram)) {
            Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN)
                    .addMsg("Konnte Server nicht starten, da nichtmehr genug RAM zur verfügung steht", LogLevel.ERROR);
            return null;
        }

        Server server;
        if(type == Server.Type.DYNAMIC || type == Server.Type.LOBBY) {
            String group = type == Server.Type.LOBBY ? "LOBBY" : template;
            String id = group + "-" + getNextFreeDynamicServerNumber(group);
            if(version != null)
                server = new DynamicServer(id, version, DEFAULT_MAX_PLAYERS, ram, (type == Server.Type.LOBBY), configManager, fileManager);
            else
                server = new DynamicServer(id, DEFAULT_MAX_PLAYERS, ram, (type == Server.Type.LOBBY), configManager, fileManager);
        } else {
            if(version != null)
                server = new StaticServer(template, version, DEFAULT_MAX_PLAYERS, ram, configManager, fileManager);
            else
                server = new StaticServer(template, DEFAULT_MAX_PLAYERS, ram, configManager, fileManager);
        }
        server.start();
        loadingServers.add(server);
        return server;
    }

    public void stopAllServer() {
        Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).addMsg("Stoppe alle Server...");

        Thread waitingThread = new Thread(() -> {
            int waitingTime = 5 * 60 * 2;

            dynamicServers.forEach(Server::stop);
            synchronized (dynamicServers) {
                while (!dynamicServers.isEmpty() && waitingTime > 0) {
                    try {
                        waitingTime--;
                        dynamicServers.wait(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            staticServers.forEach(Server::stop);
            synchronized (staticServers) {
                while (!staticServers.isEmpty() && waitingTime > 0) {
                    try {
                        waitingTime--;
                        staticServers.wait(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            lobbyServers.forEach(Server::stop);
            synchronized (lobbyServers) {
                while (!lobbyServers.isEmpty() && waitingTime > 0) {
                    try {
                        waitingTime--;
                        lobbyServers.wait(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(waitingTime <= 0) {
                Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).addMsg("Konnte nicht alle Server stoppen!", LogLevel.ERROR);
            } else {
                Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN).addMsg("Alle Server gestoppt!");
            }
        }, "Server-Stop-Waiting-Thread");

        waitingThread.start();
        try {
            waitingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeServer(Server server) {
        if(server != null) {
            ramManager.freeRam(server.getRam());

            CloudNetworkServer networkServer = Main.getAlbyCloud().getCloudNetworkServer();

            networkServer.removeClient(server.getId());
            networkServer.sendToAll(new SpigotOutUpdatePacket(server.getId(), "Stopped", 0, 0, Server.State.LOADING.id, false));
            networkServer.sendPacket("Bungee", new BungeeOutRemoveServerPacket(server.getId()));

            switch (server.getServerType()) {
                case DYNAMIC:
                    dynamicServers.remove(server);
                    break;
                case STATIC:
                    staticServers.remove(server);
                    break;
                case LOBBY:
                    lobbyServers.remove(server);
                    break;
            }
        }
    }

    public void confirmServerStart(String id) {
        getLoadingServer(id, server -> {
                loadingServers.remove(server);
                server.confirmStart();
                switch (server.getServerType()) {
                    case DYNAMIC:
                        dynamicServers.add((DynamicServer) server);
                        break;
                    case STATIC:
                        staticServers.add((StaticServer) server);
                        break;
                    case LOBBY:
                        lobbyServers.add((DynamicServer) server);
                        break;
                }
                Main.getAlbyCloud().getCloudNetworkServer().sendToAll(new SpigotOutUpdatePacket(server.getId(), server.getMotd(), 0, server.getMaxPlayers(), Server.State.LOADING.id, true));
        },
                noServer -> Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN)
                        .addMsg("Serverstartbestätigung empfangen, obwohl Server '" + id + "' nicht lädt", LogLevel.WARNING)
        );
    }

    public void disproveServerStart(String id) {
        getLoadingServer(id, server -> {
            loadingServers.remove(server);
            server.disproveStart();
        }, noServer -> Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN)
                .addMsg("Serverstartversagen empfangen, obwohl Server '" + id + "' nicht lädt", LogLevel.WARNING));
    }

    public Server getServer(String id) {
        if(id.contains("LOBBY")) {
            for(DynamicServer server : lobbyServers) {
                if(server.getId().equalsIgnoreCase(id))
                    return server;
            }
        } else {
            Server s = getDynamicServer(id);
            if(s == null)
                return getStaticServer(id);
            return s;
        }
        return null;
    }

    public DynamicServer getDynamicServer(String id) {
        for(DynamicServer server : dynamicServers) {
            if(server.getId().equalsIgnoreCase(id)) {
                return server;
            }
        }
        return null;
    }

    public StaticServer getStaticServer(String id) {
        for(StaticServer server : staticServers) {
            if(server.getId().equalsIgnoreCase(id)) {
                return server;
            }
        }
        return null;
    }

    private void getLoadingServer(String id, Consumer<Server> ifNotNull, Consumer<Void> ifNull) {
        Server loadingServer = null;
        for(Server server : loadingServers) {
            if(server.getId().equalsIgnoreCase(id))
                loadingServer = server;
        }
        if(loadingServer == null)
            ifNull.accept(null);
        else
            ifNotNull.accept(loadingServer);
    }

    private int getNextFreeDynamicServerNumber(String group) {
        int number = 0;
        do {
            number++;
        } while (existsDynamicServerWithNumber(group, number));
        return number;
    }

    private boolean existsDynamicServerWithNumber(String group, int number) {
        boolean exists = false;
        for(DynamicServer ds : dynamicServers) {
            if(ds.getId().endsWith(number + "") && ds.getId().startsWith(group))
                exists = true;
        }
        for(DynamicServer ds : lobbyServers) {
            if(ds.getId().endsWith(number + "") && ds.getId().startsWith(group))
                exists = true;
        }
        for(Server server : loadingServers) {
            if(server.getServerType() == Server.Type.DYNAMIC || server.getServerType() == Server.Type.LOBBY)
                if(server.getId().endsWith(number + "") && server.getId().startsWith(group))
                    exists = true;
        }
        return exists;
    }

    public ArrayList<DynamicServer> getDynamicServers() {
        return dynamicServers;
    }

    public ArrayList<StaticServer> getStaticServers() {
        return staticServers;
    }

    public ArrayList<DynamicServer> getLobbyServers() {
        return lobbyServers;
    }
}
