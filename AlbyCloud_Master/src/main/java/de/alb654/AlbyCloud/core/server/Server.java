package de.alb654.AlbyCloud.core.server;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.commands.commandMangers.ServerCommandManager;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.file.FileManager;
import de.alb654.AlbyCloud.screens.LogLevel;
import de.alb654.AlbyCloud.screens.Screen;
import de.alb654.AlbyCloud.screens.ScreenManager;
import de.alb654.AlbyCloud.utils.Utils;
import de.alb654.network.bungee.BungeeOutAddServerPacket;
import de.alb654.network.spigot.SpigotOutShutdownPacket;
import de.alb654.network.spigot.SpigotOutUpdatePacket;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 29.04.2020.
 */
public abstract class Server extends ExternalProcess {

    private static Screen cloudScreen;
    public static ArrayList<Integer> usedPorts = new ArrayList<>();

    private final String id;
    private final String serverVersion;
    private String motd;
    private Screen screen;
    private int maxPlayers;
    private int onlinePlayers;
    private int ram;
    private int port;

    private final Type serverType;
    private State serverState;

    private boolean isRunning;
    private boolean stopped;
    private boolean timeouted;
    private boolean groupServer;

    private File serverFolder;
    private File rootFolder;

    private final ConfigManager configManager;
    private final FileManager fileManager;

    public Server(String id, String serverVersion, int maxPlayers, int ram, Type serverType, ConfigManager configManager, FileManager fileManager) {
        this.id = id;
        this.serverVersion = serverVersion;
        this.motd = "- AlbyCloud -";
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = 0;
        this.ram = ram;

        this.serverType = serverType;
        this.serverState = State.LOADING;

        this.configManager = configManager;
        this.fileManager = fileManager;

        this.isRunning = false;
        this.timeouted = false;
        this.stopped = false;
        this.groupServer = false;

        if(cloudScreen == null)
            cloudScreen = Main.getAlbyCloud().getScreenManager().getScreen(ScreenManager.CLOUD_SCREEN);
    }

    public Server(String id, int maxPlayers, int ram, Type serverType, ConfigManager configManager, FileManager fileManager) {
        this(id, "1.12.2", maxPlayers, ram, serverType, configManager, fileManager);
    }

    protected abstract File getServerRootFolder();

    protected abstract boolean onStopped(File serverFolder);

    @Override
    protected boolean preStart() {
        cloudScreen.addMsg("Lade Dateien von '" + getId() + "'...");

        if(!isServerVersionSupported()) {
            cloudScreen.addMsg("Die Serverversion '" + serverVersion + "' ist nicht Supportet!", LogLevel.ERROR);
            return false;
        }

        if(rootFolder == null) {
            cloudScreen.addMsg("Kein Template/Save von '" + getId() + "' vorhanden. Erstelle Default...", LogLevel.WARNING);
            return createAndCopyDefaultTemplate() && replacePortInServerProperties() && setNameYMLAndPluginInPluginFolder();
        } else {
            return copyRootFolder() && replacePortInServerProperties() && setNameYMLAndPluginInPluginFolder();
        }
    }

    @Override
    protected void postStart() {
        cloudScreen.addMsg("Server '" + getId() + "' fährt hoch...");
        screen = Main.getAlbyCloud().getScreenManager().addScreen(getId(), ServerCommandManager.class);
        BufferedReader br = new BufferedReader(new InputStreamReader(getProcess().getInputStream()));

        try {

            String ln;
            while ((ln = br.readLine()) != null) {
                if (!ln.equals(">"))
                    screen.addMsg(ln);
            }

        } catch (IOException e) {
            cloudScreen.addError(e, "schreiben in Screen von '" + getId() + "'");
        }
    }

    @Override
    protected void onStop() {
        isRunning = false;
        stopped = true;

        if(getProcess() != null && getProcess().isAlive()) {
            cloudScreen.addMsg("ServerStopEvent aufgerufen, obwohl Process noch läuft! Versuche ihn zu stoppen...", LogLevel.ERROR);
            getProcess().destroy();
        }

        if(!removePortInServerProperties())
            cloudScreen.addMsg("Konnte Port in Server.Properties bei '" + getId() + "' nicht auf %port% setzen!", LogLevel.WARNING);
    }

    @Override
    protected void postStop() {
        stopped = true;

        Main.getAlbyCloud().getServerManager().removeServer(this);
        Main.getAlbyCloud().getScreenManager().deleteScreen(getId());

        if(onStopped(serverFolder)) {
            try {
                FileUtils.deleteDirectory(serverFolder);
                cloudScreen.addMsg("Server '" + getId() + "' wurde gestoppt!");
            } catch (IOException e) {
                cloudScreen.addError(e, "stoppen von '" + getId() + "'. Konnte Ordner nicht löschen");
            }
        } else {
            cloudScreen.addMsg("Server '" + getId() + "' konnte nicht gestoppt werden.");
        }
    }

    @Override
    protected void onStartFailure() {
        if(serverFolder != null) {
            try {
                FileUtils.deleteDirectory(serverFolder);
            } catch (IOException e) {
                cloudScreen.addError(e, "Löschen des Serverordners nach gescheitertem Start");
            }
        }
    }

    public void start() {
        serverFolder = setupRunningFolder();
        setupProcess(serverFolder, getJarName());

        rootFolder = getServerRootFolder();

        try {
            cloudScreen.addMsg("Starte Server '" + getId() + "'...");
            startProcess("nogui");
            startServerStartTimeout();
        } catch (NullPointerException e) {
            cloudScreen.addError(e, "starten von '" + getId() + "'");
        }
    }

    private void startServerStartTimeout() {
        new Thread(() -> {
            try {
                Thread.sleep(Integer.parseInt(configManager.getValue(ConfigManager.SERVER_START_TIMEOUT_DELAY)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!isRunning && !stopped) {
                timeouted = true;
                Main.getAlbyCloud().getServerManager().disproveServerStart(getId());
            }
        }, getId() + "-START-TIMEOUT-THREAD").start();
    }

    public void confirmStart() {
        if(timeouted)
            cloudScreen.addMsg("Server" + getId() + " hat Start bestätigt nachdem der Start-Timeout abgelaufen ist", LogLevel.ERROR);
        else {
            isRunning = true;
            cloudScreen.addMsg("Server '" + getId() + "' gestartet");

            Main.getAlbyCloud().getCloudNetworkServer().sendToAll(new SpigotOutUpdatePacket(getId(), getMotd(), getOnlinePlayers(), getMaxPlayers(), getServerState().id, true));
            Main.getAlbyCloud().getCloudNetworkServer().sendPacket("Bungee", new BungeeOutAddServerPacket(getId(), InetSocketAddress.createUnresolved("127.0.0.1", port), motd));
        }
    }

    public void disproveStart() {
        stopProcess();
        cloudScreen.addMsg("Start von '" + getId() + " fehlgeschlagen", LogLevel.ERROR);
    }

    private File setupRunningFolder() {
        return fileManager.createFolderInFolder(FileManager.Folder.SERVER, getId());
    }

    public void stop() {
        Main.getAlbyCloud().getCloudNetworkServer().sendPacket(getId(), new SpigotOutShutdownPacket());
        startServerStopTimeout();
        cloudScreen.addMsg("Stop von '" + getId() + "' angefordert!");
    }

    private void startServerStopTimeout() {
        new Thread(() -> {
            try {
                Thread.sleep(Integer.parseInt(configManager.getValue(ConfigManager.SERVER_STOP_TIMEOUT_DELAY)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isRunning) {
                stopProcess();
                cloudScreen.addMsg("Serverprozess von '" + getId() + "' nicht gestoppt! Beende Process", LogLevel.WARNING);
            }
        }, getId() + "-STOP-TIMEOUT-THREAD").start();
    }

    private void setupProcess(File serverFolder, String jarName) {
        setProcessFolder(serverFolder);
        setProcessFileName(jarName);
        setRam(ram);
    }

    protected boolean copyRootFolder() {
        try {
            FileUtils.copyDirectory(rootFolder, serverFolder);
            return true;
        } catch (IOException e) {
            cloudScreen.addError(e, "kopieren des Root-Ordners");
            return false;
        }
    }

    protected boolean createAndCopyDefaultTemplate() {
        File template = new File(fileManager.getFolder(FileManager.Folder.TEMPLATES), getId().split("-")[0]);
        template.mkdir();
        if(!(loadDefaultFile("server.properties", template) &&
                loadDefaultFile("eula.txt", template) &&
                loadDefaultFile(getJarName(), template) &&
                loadDefaultFile("spigot.yml", template) &&
                loadDefaultFile("bukkit.yml", template)))
            return false;

        rootFolder = template;
        return copyRootFolder();
    }

    private boolean loadDefaultFile(String fileName, File file) {
        try {
            fileManager.loadRessource(file, fileName);
        } catch (IOException e) {
            cloudScreen.addError(e, "laden der default " + fileName);
            cloudScreen.addMsg("Konnte '" + fileName + "' nicht laden!", LogLevel.ERROR);
            return false;
        }
        return true;
    }

    private File getServerProperties() {
        File[] serverPropList = serverFolder.listFiles(file -> file.isFile() && file.getName().contains("server.properties"));
        if(serverPropList == null || serverPropList.length < 1)
            return null;
        return serverPropList[0];
    }

    private boolean replacePortInServerProperties() {
        return replacePort(getServerProperties(), (port = nextFreePort(25566)));
    }

    private boolean setNameYMLAndPluginInPluginFolder() {
        File pluginFolder = new File(serverFolder.getAbsolutePath() + "/plugins");
        pluginFolder.mkdirs();

        File spigotPluginFolder = new File(pluginFolder, "SpigotPlugin");
        spigotPluginFolder.mkdirs();

        File nameYML = new File(spigotPluginFolder.getAbsolutePath(), "Name.yml");
        loadDefaultFile("SpigotPlugin.jar", pluginFolder);
        try {
            nameYML.createNewFile();
            Utils.writeToFile(nameYML, "Name: " + getId(), true);
            return true;
        } catch (IOException e) {
            cloudScreen.addError(e, "setzen des Name.yml files");
            return false;
        }
    }

    private boolean removePortInServerProperties() {
        return removePort(getServerProperties(), port);
    }

    private boolean replacePort(File f, int port) {
        String content;
        try {
            content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            content = content.replace("%port%", "" + port);
            Files.write(f.toPath(), content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (NullPointerException | IOException e) {
            cloudScreen.addError(e, "Ersetzen des Ports bei '" + getId() + "'");
            return false;
        }
    }

    public int nextFreePort(int from) {
        int port = from;
        while (true) {
            if (isPortFree(port)) {
                usedPorts.add(port);
                return port;
            } else {
                port++;
            }
        }
    }

    private boolean isPortFree(int port) {
        try {
            new ServerSocket(port).close();
            if(!usedPorts.contains(port)){
                return true;
            }
        } catch (IOException e) {}
        return false;
    }

    protected boolean removePort(File f, int port) {
        try {
            String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            content = content.replace(""+port, "%port%");
            Files.write(f.toPath(), content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (NullPointerException | IOException e) {
            cloudScreen.addError(e, "Ersetzen des Ports bei '" + getId() + "'");
            return false;
        }
    }

    private boolean isServerVersionSupported(){
        return getClass().getResource("/" + getJarName()) != null;
    }

    private String getJarName() {
        return "server_" + getServerVersion() +".jar";
    }

    public double getPercentageOfPlayer() {
        return ((double)onlinePlayers/(double)maxPlayers)*100;
    }

    public enum Type {
        STATIC, DYNAMIC, LOBBY
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

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public void setServerState(State serverState) {
        this.serverState = serverState;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getRam() {
        return ram;
    }

    public int getPort() {
        return port;
    }

    public Screen getScreen() {
        return screen;
    }

    public State getServerState() {
        return serverState;
    }

    public String getId() {
        return id;
    }

    public String getMotd() {
        return motd;
    }

    public Type getServerType() {
        return serverType;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isStopped() {
        return stopped;
    }

    protected FileManager getFileManager() {
        return fileManager;
    }

    protected static Screen getCloudScreen() {
        return cloudScreen;
    }

    protected File getServerFolder() {
        return serverFolder;
    }

    public void registerAsGroupServer() {
        this.groupServer = true;
    }

    public boolean isGroupServer() {
        return groupServer;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return ram == server.ram &&
                port == server.port &&
                Objects.equals(id, server.id) &&
                Objects.equals(motd, server.motd) &&
                serverType == server.serverType &&
                serverState == server.serverState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, motd, ram, port, serverType, serverState);
    }
}
