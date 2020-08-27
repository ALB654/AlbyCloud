package de.alb654.AlbyBungeePlugin.report;/*
 * Copyright (c)
 * Created by Aaron Bernert on 26.8.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.alb654.AlbyBungeePlugin.BungeePlugin;
import de.alb654.AlbyBungeePlugin.utils.NameFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReportManager {

    private final MongoCollection<Document> reportCollection;

    public ReportManager(MongoCollection<Document> reportCollection) {
        this.reportCollection = reportCollection;

        ProxyServer.getInstance().getScheduler().schedule(BungeePlugin.getPlugin(), this::checkActiveReports, 1, TimeUnit.MINUTES);
    }

    private void checkActiveReports() {
        for(Document doc : reportCollection.find()) {
            if(doc.getBoolean("ACTIVE")) {
                long startTime = doc.getLong("StartCheckTime");
                if(startTime != -1 && System.currentTimeMillis() - startTime > TimeUnit.MINUTES.toMillis(15)) {
                    reportCollection.updateOne(Filters.eq("UUID", doc.getString("UUID")), Updates.set("ACTIVE", false));
                    reportCollection.updateOne(Filters.eq("UUID", doc.getString("UUID")), Updates.set("CheckedBy", "*"));
                    reportCollection.updateOne(Filters.eq("UUID", doc.getString("UUID")), Updates.set("StartCheckTime", -1L));

                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(doc.getString("CheckedBy")));
                    if(player != null) {
                        player.sendMessage(BungeePlugin.Bprefix + "§cDeine Reportbearbeitungszeit ist abgelaufen!");
                    }
                }
            }
        }
    }

    public synchronized void acceptReport(UUID reported, UUID accepted) {
        Document report = reportCollection.find(Filters.eq("UUID", reported.toString())).first();
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(accepted);

        if(report != null) {
            if(!report.getBoolean("ACTIVE")) {
                reportCollection.updateOne(Filters.eq("UUID", report.getString("UUID")), Updates.set("ACTIVE", true));
                reportCollection.updateOne(Filters.eq("UUID", report.getString("UUID")), Updates.set("CheckedBy", accepted.toString()));
                reportCollection.updateOne(Filters.eq("UUID", report.getString("UUID")), Updates.set("StartCheckTime", System.currentTimeMillis()));

                sendMsg(player, "§7Du hast den Report von §2" + NameFetcher.getName(reported) + " §7angenommen!");
                sendMsg(player, "§7Du hast §215min Zeit §7ihn zu bearbeiten!");
            } else
                sendMsg(player, "§cDer Report wird schon bearbeitet!");
        } else
            sendMsg(player, "§cDer Report existiert nicht mehr!");
    }

    public void broadcastReportMessage(String reportedName, String reporterName, String reason) {
        BungeePlugin.getPlugin().getReportMsgCommand().getReceiver().forEach(receiver -> {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(receiver));

            sendMsg(player, " ");
            sendMsg(player, "§7-------------------------");
            sendMsg(player, "§2§l" + reportedName + "§7 wurde von §2§l" + reporterName + " §7für §2" + reason + "§7 reportet!");
            sendMsg(player, "§7-------------------------");
            sendMsg(player, " ");
        });
    }

    public synchronized void requestReportDelete(UUID reported) {
        reportCollection.deleteOne(Filters.eq("UUID", reported.toString()));
    }

    private void sendMsg(ProxiedPlayer player, String msg) {
        if(player != null)
            player.sendMessage(new ComponentBuilder(BungeePlugin.Bprefix + msg).create());
    }
}
