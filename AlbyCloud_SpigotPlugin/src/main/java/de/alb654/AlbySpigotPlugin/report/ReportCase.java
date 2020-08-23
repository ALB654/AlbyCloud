package de.alb654.AlbySpigotPlugin.report;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.8.2020
 */

import org.bson.Document;

import java.util.ArrayList;

public class ReportCase {

    private final String name;
    private final String uuid;

    private final ArrayList<String> reporterAndReasons;

    private boolean active;
    private String checkedByUUID;

    public ReportCase(String name, String uuid, ArrayList<String> reporterAndReasons) {
        this.name = name;
        this.uuid = uuid;
        if(reporterAndReasons != null)
            this.reporterAndReasons = reporterAndReasons;
        else
            this.reporterAndReasons = new ArrayList<>();

        this.active = false;
        this.checkedByUUID = "*";
    }

    public ReportCase(Document doc) {
        this(doc.getString("NAME"), doc.getString("UUID"), new ArrayList<>(doc.getList("BYs", String.class)));

        this.active = doc.getBoolean("ACTIVE");
        this.checkedByUUID = doc.getString("CheckedBy");
    }

    public Document getAsDocument() {
        return new Document("NAME", name)
                .append("UUID", uuid)
                .append("BYs", reporterAndReasons)
                .append("ACTIVE", active)
                .append("CheckedBy", checkedByUUID);
    }

    public void setCheckedBy(String uuid) {
        this.active = true;
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
        return active;
    }
}
