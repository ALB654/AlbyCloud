package de.alb654.AlbySpigotPlugin.report;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.8.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportCase {

    private final String name;
    private final String uuid;

    private ArrayList<UUIDReason> reporterAndReasons;

    private boolean newReport;
    private boolean checking;
    private String checkedByUUID;
    private long startedCheckingTime;

    public ReportCase(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
        this.reporterAndReasons = new ArrayList<>();
        this.newReport = true;
        this.checking = false;
        this.checkedByUUID = "*";
        this.startedCheckingTime = -1;
    }

    public ReportCase(Document doc) {
        this(doc.getString("NAME"), doc.getString("UUID"));

        this.newReport = false;
        this.checking = doc.getBoolean("ACTIVE");
        this.checkedByUUID = doc.getString("CheckedBy");
        this.startedCheckingTime = doc.getLong("StartCheckTime");
        this.reporterAndReasons = getReportsListFromStringArray(doc.getList("BYs", String.class));
    }

    public Document getAsDocument() {
        return new Document("NAME", name)
                .append("UUID", uuid)
                .append("BYs", reportListToStringArray())
                .append("ACTIVE", checking)
                .append("CheckedBy", checkedByUUID)
                .append("StartCheckTime", startedCheckingTime);
    }

    public void saveChangesInDB(MongoCollection<Document> collection) {
        if(isNewReport()) {
            collection.insertOne(getAsDocument());
        } else{
            collection.replaceOne(Filters.eq("UUID", uuid), getAsDocument());
        }
    }

    public void addReport(String uuid, String reason) {
        reporterAndReasons.add(new UUIDReason(uuid, reason));
    }

    @SuppressWarnings("unchecked")
    public ArrayList<UUIDReason> getReporterAndReasons() {
        return (ArrayList<UUIDReason>) reporterAndReasons.clone();
    }

    public void setCheckedBy(String uuid) {
        this.checking = true;
        this.checkedByUUID = uuid;
        this.startedCheckingTime = System.currentTimeMillis();
    }

    private ArrayList<UUIDReason> getReportsListFromStringArray(List<String> stringArray) {
        ArrayList<UUIDReason> arr = new ArrayList<>();
        stringArray.forEach(s -> arr.add(new UUIDReason(s.split(";")[0], s.split(";")[1])));
        return arr;
    }

    public ArrayList<String> reportListToStringArray() {
        ArrayList<String> arr = new ArrayList<>();
        reporterAndReasons.forEach(ru -> arr.add(ru.toString()));
        return arr;
    }

    public String getName() {
        return name;
    }

    public String getCheckedByUUID() {
        return checkedByUUID;
    }

    public String getUuid() {
        return uuid;
    }

    public long getStartedCheckingTime() {
        return startedCheckingTime;
    }

    public boolean isActive() {
        return checking;
    }

    public boolean isNewReport() {
        return newReport;
    }

    public static class UUIDReason implements Serializable {

        private final String uuid;
        private final String reason;

        public UUIDReason(String uuid, String reason) {
            this.uuid = uuid;
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

        public String getUuid() {
            return uuid;
        }

        @Override
        public String toString() {
            return uuid + ";" + reason;
        }
    }
}
