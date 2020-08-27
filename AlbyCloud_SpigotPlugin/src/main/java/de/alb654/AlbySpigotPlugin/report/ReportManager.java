package de.alb654.AlbySpigotPlugin.report;
/*
 * Copyright (c)
 * Created by Aaron Bernert on 23.8.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.recent.RecentReportAction;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class ReportManager {

    private final MongoCollection<Document> reportsCollection;

    public ReportManager(MongoManager mongoManager) {
        reportsCollection = mongoManager.getCollection(MongoManager.REPORTS);
    }

    public void addReport(String uuid, String reporterUUID, String reason) {
        ReportCase rCase = getReport(uuid);
        rCase.addReport(reporterUUID, reason);
        rCase.saveChangesInDB(reportsCollection);

        CloudApi.getInstance().getBungeeConnection().sendMessage("report", "msg", NameFetcher.getName(UUID.fromString(uuid)), NameFetcher.getName(UUID.fromString(reporterUUID)), reason);
    }

    public void acceptReport(String acceptorUUID, String reportUUID) {
        ReportCase rCase = getReport(reportUUID);
        if(!rCase.isNewReport()) {
            CloudApi.getInstance().getBungeeConnection().sendMessage("report", "accept", reportUUID, acceptorUUID);
        }
    }

    public void deleteReport(ReportCase rCase, String deleterName) {
        CloudApi.getInstance().getBungeeConnection().sendMessage("report", "delete", rCase.getUuid());
        SpigotPlugin.getSpigotPlugin().getRecentActionsManager().addRecentAction(new RecentReportAction(
                System.currentTimeMillis(), rCase.getUuid(), deleterName, rCase.reportListToStringArray(), false, true));
    }

    public void agreeReport(String agreerName, ReportCase rCase) {
        CloudApi.getInstance().getBungeeConnection().sendMessage("report", "delete", rCase.getUuid());
        SpigotPlugin.getSpigotPlugin().getRecentActionsManager().addRecentAction(new RecentReportAction(
                System.currentTimeMillis(), rCase.getUuid(), agreerName, rCase.reportListToStringArray(), true, false));
    }

    public void disagreeReport(String agreerName, ReportCase rCase) {
        CloudApi.getInstance().getBungeeConnection().sendMessage("report", "delete", rCase.getUuid());
        SpigotPlugin.getSpigotPlugin().getRecentActionsManager().addRecentAction(new RecentReportAction(
                System.currentTimeMillis(), rCase.getUuid(), agreerName, rCase.reportListToStringArray(), false, false));
    }

    public ArrayList<ReportCase> getReports() {
        ArrayList<ReportCase> cases = new ArrayList<>();
        reportsCollection.find().forEach((Consumer<? super Document>) doc -> cases.add(new ReportCase(doc)));
        return cases;
    }

    public ReportCase getActiveReportFrom(String checkerUUID) {
        Document doc = reportsCollection.find(Filters.eq("CheckedBy", checkerUUID)).first();
        if(doc != null)
            return new ReportCase(doc);
        return null;
    }

    public ReportCase getReport(String uuid) {
        Document doc = reportsCollection.find(Filters.eq("UUID", uuid)).first();
        if(doc == null)
            return new ReportCase(NameFetcher.getName(UUID.fromString(uuid)), uuid);
        else
            return new ReportCase(doc);
    }

}
