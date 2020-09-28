package de.alb654.AlbyCloud.core.server;

import de.alb654.AlbyCloud.Main;
import de.alb654.AlbyCloud.screens.LogLevel;

import java.io.*;

/**
 * Copyright (c)
 * Created by Aaron Bernert on 29.04.2020.
 */
public abstract class ExternalProcess {

    private File processFolder;
    private String processFileName;
    private ProcessBuilder processBuilder;
    private Process process;
    private Writer writer;

    private Thread processThread;

    private int ram = 0;

    protected abstract void onStop();

    protected abstract void postStop();

    protected abstract void postStart();

    protected abstract boolean preStart();

    protected abstract void onStartFailure();

    protected void startProcess(String arg) throws NullPointerException {
        if(processFileName == null || processFolder == null) {
            throw new NullPointerException("ProcessFileName or ProcessFile equal null");
        }

        processThread = new Thread(() -> {
            if(preStart()) {
                processBuilder = new ProcessBuilder("java", "-Xmx" + ram + "M", "-jar", processFileName, arg).directory(processFolder);

                try {
                        process = processBuilder.start();
                        writer = new OutputStreamWriter(process.getOutputStream(), "UTF-8");

                        postStart();
                        process.waitFor();
                } catch (IOException | InterruptedException e) {
                    Main.getAlbyCloud().getScreenManager().getActiveScreen().addError(e, "ausführen von '" + processFileName + "' in '" + processThread.getName() + "'");
                }

                onStop();
            } else {
                Main.getAlbyCloud().getScreenManager().getActiveScreen().addMsg("Konnte Process nicht starten!", LogLevel.ERROR);
                onStartFailure();
            }

            postStop();
        }, "Process-" + processFileName + "-Thread");

        processThread.start();
    }

    protected void stopProcess() {
        process.destroyForcibly();
    }

    public void executeCommand(String cmd) {

        try {
            writer.write(cmd + "\n");
        } catch (IOException e) {
            Main.getAlbyCloud().getScreenManager().getActiveScreen().addError(e, "ausführen von Command '" + cmd + "' im ExternalProcess'");
        }

    }

    public Process getProcess() {
        return process;
    }

    protected void setProcessFolder(File processFolder) {
        this.processFolder = new File(processFolder.getAbsolutePath().replace("\\.", ""));
    }

    protected void setProcessFileName(String processFileName) {
        this.processFileName = processFileName;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }
}
