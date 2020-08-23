package de.alb654.AlbyCloud.core.server.statics;

import de.alb654.AlbyCloud.core.server.Server;
import de.alb654.AlbyCloud.file.ConfigManager;
import de.alb654.AlbyCloud.file.FileManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StaticServer extends Server {

    public StaticServer(String id, int maxPlayers, int ram, ConfigManager configManager, FileManager fileManager) {
        super(id, maxPlayers, ram, Type.STATIC, configManager, fileManager);
    }

    public StaticServer(String id, String serverVersion, int maxPlayers, int ram, ConfigManager configManager, FileManager fileManager) {
        super(id, serverVersion, maxPlayers, ram, Type.STATIC, configManager, fileManager);
    }

    @Override
    protected File getServerRootFolder() {
        try {
            return Objects.requireNonNull(getFileManager().getFolder(FileManager.Folder.SAVES).listFiles(file -> file.isDirectory() && file.getName().equals(getId())))[0];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected boolean onStopped(File serverFolder) {
        File saveFile = new File(getFileManager().getFolder(FileManager.Folder.SAVES), getId());
        boolean exists = true;
        if(!saveFile.exists())
            exists = saveFile.mkdir();

        if(exists) {
            try {
                FileUtils.copyDirectory(getServerFolder(), saveFile);
            } catch (IOException e) {
                return false;
            }
        } else
            return false;

        return true;
    }
}
