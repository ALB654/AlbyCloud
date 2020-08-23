package de.alb654.AlbySpigotPlugin.recent;

import org.bson.Document;

import java.util.Objects;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

public abstract class RecentAction {

    protected final long actionTime;
    protected final String suspectUUID;
    protected final ActionType type;

    protected RecentAction(long actionTime, String suspectUUID, ActionType type) {
        this.actionTime = actionTime;
        this.suspectUUID = suspectUUID;
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }

    public long getActionTime() {
        return actionTime;
    }

    public String getSuspectUUID() {
        return suspectUUID;
    }

    public Document toMongoDoc() {
        return new Document("actionTime", actionTime)
                .append("suspectUUID", suspectUUID)
                .append("type", type.name());
    }

    public enum ActionType {
        BAN, UNBAN, REPORT
    }

    @Override
    public String toString() {
        return "RecentAction{" +
                "actionTime=" + actionTime +
                ", suspectUUID='" + suspectUUID + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecentAction that = (RecentAction) o;
        return actionTime == that.actionTime &&
                Objects.equals(suspectUUID, that.suspectUUID) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionTime, suspectUUID, type);
    }
}
