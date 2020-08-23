package de.alb654.AlbySpigotPlugin.recent;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

import org.bson.Document;

import java.util.ArrayList;
import java.util.Objects;

public class RecentReportAction extends RecentAction {

    private final String processedBy;
    private final ArrayList<String> reportsFrom;

    public RecentReportAction(long actionTime, String suspectUUID, String processedBy, ArrayList<String> reportsFrom) {
        super(actionTime, suspectUUID, ActionType.REPORT);
        this.processedBy = processedBy;
        this.reportsFrom = reportsFrom;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public ArrayList<String> getReportsFrom() {
        return reportsFrom;
    }

    @Override
    public Document toMongoDoc() {
        return super.toMongoDoc().append("ProcessedBy", processedBy)
                                 .append("reportsFrom", reportsFrom);
    }

    public static RecentReportAction createFromMongoDoc(Document doc) {
        return new RecentReportAction(doc.getLong("actionTime"), doc.getString("suspectUUID"),
                                doc.getString("ProcessedBy"), new ArrayList<>(doc.getList("reportsFrom", String.class)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RecentReportAction that = (RecentReportAction) o;
        return Objects.equals(processedBy, that.processedBy) &&
                Objects.equals(reportsFrom, that.reportsFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), processedBy, reportsFrom);
    }

    @Override
    public String toString() {
        return "RecentReportAction{" +
                "processedBy='" + processedBy + '\'' +
                ", reportFrom='" + reportsFrom + '\'' +
                ", actionTime=" + actionTime +
                ", suspectUUID='" + suspectUUID + '\'' +
                ", type=" + type +
                '}';
    }
}
