package de.alb654.AlbyCloud.file;

import de.alb654.AlbyCloud.Main;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 15.10.2019.
 */
public class FileManager {

    private final String path;
    private File cloudFolder;

    private final ArrayList<File> folders = new ArrayList<>();

    public FileManager(String path) {
        this.path = path;

        setup();

        addFolders();
    }

    private void addFolders() {
        for(Folder f : Folder.values()) {
            addFolder(WordUtils.capitalize(f.name().toLowerCase()));
        }
    }

    private void setup() {
        cloudFolder = new File(path);
        if(!cloudFolder.exists())
            cloudFolder.mkdirs();
    }

    private void addFolder(String name) {
        File folder = new File(cloudFolder, name);
        if(!folder.exists()) folder.mkdirs();
        folders.add(folder);
    }

    public File getFolder(Folder folder) {
        String name = WordUtils.capitalize(folder.name().toLowerCase());
        for(File f : folders) if(f.getName().equals(name)) return f;
        File f = new File(cloudFolder, name);
        f.mkdir();
        return f;
    }

    public File createFileInFolder(Folder folder, String fileName, String defaultFile) {
        File file = new File(getFolder(folder), fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();

                if(defaultFile != null) {
                    loadRessource(getFolder(folder), defaultFile, fileName);
                }
            } catch (IOException e) {
                Main.getAlbyCloud().getScreenManager().getActiveScreen().addError(e, "erstellen der Datei '" + fileName + "'");
            }
        }
        return file;
    }

    public File createFileInFolder(File folder, String fileName) {
        File file = new File(folder, fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Main.getAlbyCloud().getScreenManager().getActiveScreen().addError(e, "erstellen der Datei '" + fileName + "'");
            }
        }
        return file;
    }

    public File createFolderInFolder(Folder folder, String folderName) {
        File file = new File(getFolder(folder), folderName);
        if(!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public boolean containsFolderFile(File folder, String name) {
        if(folder.exists()) {
            for(String file : folder.list()) {
                if(file.equals(name))
                    return true;
            }
        }
        return false;
    }

    public void loadRessource(File folder, String fileName, String newFileName) throws IOException {
        URL url = getClass().getResource("/" + fileName);
        File output = new File(folder, newFileName);
        FileUtils.copyURLToFile(url, output);
    }

    public void loadRessource(File folder, String fileName) throws IOException {
        loadRessource(folder, fileName, fileName);
    }

    public enum Folder {
        CONFIG, SERVER, TEMPLATES, SAVES, BUNGEE, LOG
    }
}
