package de.alb654.AlbyBungeePlugin.network.handle.handler.spigot;/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.8.2020
 */


import de.alb654.AlbyBungeePlugin.network.SpigotConnection;
import de.alb654.AlbyBungeePlugin.report.ReportManager;

import java.util.UUID;

public class ReportHandler implements SpigotConnection.PluginMessageHandler {

    private final ReportManager reportManager;

    public ReportHandler(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public void handleMessage(String[] args) {
        synchronized (reportManager) {
            if(args[0].contains("accept")) {
                String reportUUID = args[1];
                String acceptor = args[2];

                reportManager.acceptReport(UUID.fromString(reportUUID), UUID.fromString(acceptor));
            } else if(args[0].contains("msg")) {
                String reportedName = args[1];
                String reporterName = args[2];
                String reason = args[3];

                reportManager.broadcastReportMessage(reportedName, reporterName, reason);
            } else if(args[0].contains("delete")) {
                String reportUUID = args[1];

                reportManager.requestReportDelete(UUID.fromString(reportUUID));
            }
        }
    }

}
