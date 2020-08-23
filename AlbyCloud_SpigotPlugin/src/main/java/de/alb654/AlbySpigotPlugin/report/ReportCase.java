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

public class ReportCase {

    private final String name;
    private final String uuid;

    private final ArrayList<UUIDReason> reporterAndReasons;

    private boolean newReport;
    private boolean checking;
    private String checkedByUUID;

    public ReportCase(String name, String uuid, ArrayList<UUIDReason> reporterAndReasons) {
        this.name = name;
        this.uuid = uuid;
        if(reporterAndReasons != null)
            this.reporterAndReasons = reporterAndReasons;
        else
            this.reporterAndReasons = new ArrayList<>();

        this.newReport = false;
        this.checking = false;
        this.checkedByUUID = "*";
    }

    public ReportCase(Document doc) {
        this(doc.getString("NAME"), doc.getString("UUID"), new ArrayList<>(doc.getList("BYs", UUIDReason.class)));

        this.newReport = true;
        this.checking = doc.getBoolean("ACTIVE");
        this.checkedByUUID = doc.getString("CheckedBy");
    }

    public Document getAsDocument() {
        return new Document("NAME", name)
                .append("UUID", uuid)
                .append("BYs", reporterAndReasons)
                .append("ACTIVE", checking)
                .append("CheckedBy", checkedByUUID);
    }

    public void saveChangesInDB(MongoCollection<Document> collection) {
        if(isNewReport()) {
            collection.insertOne(getAsDocument());
        } else{
            collection.updateOne(Filters.eq("UUID", uuid), getAsDocument());
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

    public boolean isActive() {
        return checking;
    }

    public boolean isNewReport() {
        return newReport;
    }

    public class UUIDReason implements Serializable {
        private String uuid;
        private String reason;

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
    }
}
