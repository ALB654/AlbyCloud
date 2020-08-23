package de.alb654.AlbyCloud.file;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.utils.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 23.04.2020.
 */
public class ConfigManager {

    private File configFile;

    public static final String RAM = "RAM";
    public static final String BUNGEE_RAM = "BUNGEE_RAM";
    public static final String LOG_AMOUNT = "LOG_AMOUNT";
    public static final String SERVER_STOP_TIMEOUT_DELAY = "SERVER_STOP_TIMEOUT_DELAY";
    public static final String SERVER_START_TIMEOUT_DELAY = "SERVER_START_TIMEOUT_DELAY";
    public static final String BUNGEE_START_TIMEOUT_DELAY = "BUNGEE_START_TIMEOUT_DELAY";

    public ConfigManager(FileManager fileManager, String fileName) {

        setup(fileManager, fileName);
    }

    private void setup(FileManager fileManager, String fileName) {
        configFile = fileManager.createFileInFolder(FileManager.Folder.CONFIG, fileName, fileName);
    }

    public ConfigGetter getUpdatingValue(String key) {
        return () -> {
            Object obj = getObject().get(key);
            return obj == null ? "N/A" : (String) obj;
        };
    }

    public String getValue(String key) {
        Object obj = getObject().get(key);
        return obj == null ? "N/A" : (String) obj;
    }

    public void changeValue(String key, String value) {
        JSONObject obj = getObject();
        obj.put(key, value);
        writeObject(obj);
    }

    public boolean has(String key) {
        return !getValue(key).equals("null");
    }

    private JSONObject getObject() {
        try(FileReader r = new FileReader(configFile)) {
            return (JSONObject) new JSONParser().parse(r);
        } catch (IOException | ParseException e) {
            Main.getAlbyCloud().getScreenManager().getActiveScreen().addError(e, "lesen der Config.json");
        }
        return new JSONObject();
    }

    private void writeObject(JSONObject object) {
        Utils.writeToFile(configFile, object.toJSONString(), true);
    }
}
