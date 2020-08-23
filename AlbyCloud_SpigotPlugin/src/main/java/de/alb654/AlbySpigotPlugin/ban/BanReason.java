package de.alb654.AlbySpigotPlugin.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.7.2020
 */

import org.bson.Document;

import java.util.Objects;

public class BanReason {

    private final String reason;
    private final long banTime;
    private final boolean mute;

    private BanReason(String reason, long banTime, boolean mute) {
        this.reason = reason;
        this.banTime = banTime;
        this.mute = mute;
    }

    public long getBanTime() {
        return banTime;
    }

    public String getReason() {
        return reason;
    }

    public boolean isMute() {
        return mute;
    }

    public Document toMongoDoc(){
        return new Document("Reason", reason)
                .append("banTime", banTime)
                .append("mute", mute);
    }

    public static BanReason createFromDoc(Document doc){
        return new BanReason(doc.getString("Reason"),
                            doc.getLong("banTime"),
                            doc.getBoolean("mute"));
    }

    public static BanReason createCustom(String reason, long banTime, boolean mute) {
        return new BanReason(reason, banTime, mute);
    }

    @Override
    public String toString() {
        return "BanReason{" +
                "reason='" + reason + '\'' +
                ", banTime=" + banTime +
                ", mute=" + mute +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanReason banReason = (BanReason) o;
        return banTime == banReason.banTime &&
                Objects.equals(reason, banReason.reason) &&
                mute == banReason.mute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, banTime, mute);
    }

}
