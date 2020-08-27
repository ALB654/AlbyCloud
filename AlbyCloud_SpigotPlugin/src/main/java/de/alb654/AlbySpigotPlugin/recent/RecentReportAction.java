package de.alb654.AlbySpigotPlugin.recent;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

import org.bson.Document;

import java.util.ArrayList;
import java.util.Objects;

public class RecentReportAction extends RecentAction {

    private final String processedByName;
    private final ArrayList<String> reportsReasons;
    private final boolean banned;
    private final boolean deleted;

    public RecentReportAction(long actionTime, String suspectUUID, String processedByName, ArrayList<String> reportsReasons, boolean banned, boolean deleted) {
        super(actionTime, suspectUUID, ActionType.REPORT);
        this.processedByName = processedByName;
        this.reportsReasons = reportsReasons;
        this.banned = banned;
        this.deleted = deleted;
    }

    public String getProcessedByName() {
        return processedByName;
    }

    public ArrayList<String> getReportsReasons() {
        return reportsReasons;
    }

    public boolean isBanned() {
        return banned;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public Document toMongoDoc() {
        return super.toMongoDoc().append("processedByName", processedByName)
                                 .append("reportsReasons", reportsReasons)
                                 .append("banned", banned)
                                 .append("deleted", deleted);
    }

    public static RecentReportAction createFromMongoDoc(Document doc) {
        return new RecentReportAction(doc.getLong("actionTime"), doc.getString("suspectUUID"),
                                doc.getString("processedByName"), new ArrayList<>(doc.getList("reportsReasons", String.class)),
                                doc.getBoolean("banned"), doc.getBoolean("deleted"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RecentReportAction that = (RecentReportAction) o;
        return Objects.equals(processedByName, that.processedByName) &&
                Objects.equals(reportsReasons, that.reportsReasons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), processedByName, reportsReasons, banned, deleted);
    }

    @Override
    public String toString() {
        return "RecentReportAction{" +
                "processedByName='" + processedByName + '\'' +
                ", reportsReasons='" + reportsReasons + '\'' +
                ", actionTime=" + actionTime +
                ", suspectUUID='" + suspectUUID + '\'' +
                ", type=" + type +
                '}';
    }
}
