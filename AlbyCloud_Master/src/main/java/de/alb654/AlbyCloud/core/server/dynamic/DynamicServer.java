package de.alb654.AlbyCloud.core.server.dynamic;

import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.file.FileManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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

    public void safeCurrentWorldAsTemplate() {
        File serverFolder = getServerFolder();
        File rootFolder = getServerRootFolder();

        try {
            FileUtils.copyDirectory(serverFolder, rootFolder);
        } catch (IOException e) {
            getCloudScreen().addError(e, "kopieren des Server zum Root-Ordner");
        }

        File serverProp = null;
        File[] files = rootFolder.listFiles();
        if(rootFolder.exists() && files != null && files.length > 0)
            for(File file : files) {
                if(file.getName().equals("server.properties")) {
                    serverProp = file;
                    break;
                }
            }
        else
            return;

        if(serverProp != null)
            removePort(serverProp, getPort());
    }

}
