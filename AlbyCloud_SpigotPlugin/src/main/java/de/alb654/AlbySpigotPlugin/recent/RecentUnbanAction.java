package de.alb654.AlbySpigotPlugin.recent;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

import org.bson.Document;

import java.util.Objects;

public class RecentUnbanAction extends RecentAction {

    private final String unbanBy;
    private final long normalBanUntil;
    private final String normalBanBy;
    private final String banReason;

    public RecentUnbanAction(long actionTime, String suspectUUID, String unbanBy, long normalBanUntil, String normalBanBy, String banReason) {
        super(actionTime, suspectUUID, ActionType.UNBAN);
        this.unbanBy = unbanBy;
        this.normalBanUntil = normalBanUntil;
        this.normalBanBy = normalBanBy;
        this.banReason = banReason;
    }

    public String getUnbanBy() {
        return unbanBy;
    }

    public long getNormalBanUntil() {
        return normalBanUntil;
    }

    public String getBanReason() {
        return banReason;
    }

    public String getNormalBanBy() {
        return normalBanBy;
    }

    public static RecentUnbanAction createFromMongoDoc(Document doc) {
        return new RecentUnbanAction(doc.getLong("actionTime"), doc.getString("suspectUUID"),
                    doc.getString("unbanBy"), doc.getLong("normalBanUntil"),
                doc.getString("normalBanBy"), doc.getString("banReason"));
    }

    @Override
    public Document toMongoDoc() {
        return super.toMongoDoc().append("unbanBy", unbanBy)
                        .append("normalBanUntil", normalBanUntil)
                        .append("normalBanBy", normalBanBy)
                        .append("banReason", banReason);
    }

    @Override
    public String toString() {
        return "RecentUnbanAction{" +
                "unbanBy='" + unbanBy + '\'' +
                ", normalBanUntil=" + normalBanUntil +
                ", normalBanBy='" + normalBanBy + '\'' +
                ", banReason='" + banReason + '\'' +
                ", actionTime=" + actionTime +
                ", suspectUUID='" + suspectUUID + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RecentUnbanAction that = (RecentUnbanAction) o;
        return normalBanUntil == that.normalBanUntil &&
                Objects.equals(unbanBy, that.unbanBy) &&
                Objects.equals(normalBanBy, that.normalBanBy) &&
                Objects.equals(banReason, that.banReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), unbanBy, normalBanUntil, normalBanBy, banReason);
    }
}
