package de.alb654.AlbyCloud.core.server.dynamic;

import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.file.FileManager;

import java.io.File;
import java.util.Objects;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 12.05.2020.
 */
public class DynamicServer extends Server {

    public DynamicServer(String id, int maxPlayers, int ram, boolean isLobby, ConfigManager configManager, FileManager fileManager) {
        super(id, maxPlayers, ram, isLobby ? Type.LOBBY : Type.DYNAMIC, configManager, fileManager);
    }

    public DynamicServer(String id, String serverVersion, int maxPlayers, int ram, boolean isLobby, ConfigManager configManager, FileManager fileManager) {
        super(id, serverVersion, maxPlayers, ram, isLobby ? Type.LOBBY : Type.DYNAMIC, configManager, fileManager);
    }

    @Override
    protected File getServerRootFolder() {
        try {
            return Objects.requireNonNull(getFileManager().getFolder(FileManager.Folder.TEMPLATES).listFiles(file -> file.isDirectory() && file.getName().equals(getId().split("-")[0])))[0];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected boolean onStopped(File serverFolder) {
        return true;
    }
}
