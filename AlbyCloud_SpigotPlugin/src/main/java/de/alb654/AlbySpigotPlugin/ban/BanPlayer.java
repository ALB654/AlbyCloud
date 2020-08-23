package de.alb654.AlbySpigotPlugin.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.7.2020
 */

import org.bson.Document;

import java.util.Objects;

public class BanPlayer {

    private final String name;
    private final String uuid;
    private final String byName;
    private final long untilTime;
    private final long banTime;
    private final String reason;
    private final boolean isMute;

    public BanPlayer(String name, String uuid, String byName, long untilTime, long banTime, String reason, boolean isMute) {
        this.name = name;
        this.uuid = uuid;
        this.byName = byName;
        this.untilTime = untilTime;
        this.banTime = banTime;
        this.reason = reason;
        this.isMute = isMute;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getByName() {
        return byName;
    }

    public long getUntilTime() {
        return untilTime;
    }

    public long getBanTime() {
        return banTime;
    }

    public String getReason() {
        return reason;
    }

    public boolean isMute() {
        return isMute;
    }

    public Document toMongoDoc() {
        return new Document("Name", name)
                .append("UUID", uuid)
                .append("byName", byName)
                .append("untilTime", untilTime)
                .append("banTime", banTime)
                .append("reason", reason)
                .append("isMute", isMute);
    }

    public static BanPlayer createFromMongoDocument(Document doc) {
        return new BanPlayer(doc.getString("Name"),
                             doc.getString("UUID"),
                            doc.getString("byName"),
                            doc.getLong("untilTime"),
                            doc.getLong("banTime"),
                            doc.getString("reason"),
                            doc.getBoolean("isMute"));
    }

    @Override
    public String toString() {
        return "BanPlayer{" +
                "name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", byName='" + byName + '\'' +
                ", untilTime=" + untilTime +
                ", banTime=" + banTime +
                ", reason='" + reason + '\'' +
                ", isMute=" + isMute +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanPlayer banPlayer = (BanPlayer) o;
        return untilTime == banPlayer.untilTime &&
                banTime == banPlayer.banTime &&
                isMute == banPlayer.isMute &&
                name.equals(banPlayer.name) &&
                uuid.equals(banPlayer.uuid) &&
                byName.equals(banPlayer.byName) &&
                reason.equals(banPlayer.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid, byName, untilTime, banTime, reason, isMute);
    }
}
