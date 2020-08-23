package de.alb654.AlbySpigotPlugin.report;
/*
 * Copyright (c)
 * Created by Aaron Bernert on 23.8.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
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
    }

    public void acceptReport(String checkerUUID, String reportUUID) {
        ReportCase rCase = getReport(reportUUID);
        if(!rCase.isNewReport()) {
            rCase.setCheckedBy(checkerUUID);
            rCase.saveChangesInDB(reportsCollection);
        }
    }

    public void deleteReport(String uuid) {
        reportsCollection.deleteOne(Filters.eq("UUID", uuid));
    }

    public ArrayList<ReportCase> getReports() {
        ArrayList<ReportCase> cases = new ArrayList<>();
        reportsCollection.find().forEach((Consumer<? super Document>) doc -> cases.add(new ReportCase(doc)));
        return cases;
    }

    public ReportCase getReport(String uuid) {
        Document doc = reportsCollection.find(Filters.eq("UUID", uuid)).first();
        if(doc == null)
            return new ReportCase(NameFetcher.getName(UUID.fromString(uuid)), uuid, null);
        else
            return new ReportCase(doc);
    }

}
