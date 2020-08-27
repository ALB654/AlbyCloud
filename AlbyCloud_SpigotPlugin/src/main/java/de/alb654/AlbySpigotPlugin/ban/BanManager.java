package de.alb654.AlbySpigotPlugin.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.7.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.recent.RecentBanAction;
import de.alb654.AlbySpigotPlugin.recent.RecentUnbanAction;
import de.alb654.AlbySpigotPlugin.report.ReportCase;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class BanManager {

    private final MongoCollection<Document> bannedPlayerCollection;
    private final MongoCollection<Document> bannedReasonCollection;

    public BanManager(MongoManager mongoManager) {
        bannedPlayerCollection = mongoManager.getCollection(MongoManager.BANNEDPLAYER);
        bannedReasonCollection = mongoManager.getCollection(MongoManager.BANREASONS);
    }

    public boolean banPlayer(String name, BanReason reason, String byName) {
        UUID uuid = NameFetcher.getUUID(name);
        if(uuid != null && !isBanned(uuid.toString())){

            long untilTime = reason.getBanTime() < 0 ? -1 : System.currentTimeMillis() + reason.getBanTime();

            BanPlayer bp = new BanPlayer(name, uuid.toString(), byName,
                    untilTime, System.currentTimeMillis(), reason.getReason(), reason.isMute());

            bannedPlayerCollection.insertOne(bp.toMongoDoc());
            SpigotPlugin.getSpigotPlugin().getRecentActionsManager().addRecentAction(new RecentBanAction(System.currentTimeMillis(), uuid.toString()));

            CloudApi.getInstance().getBungeeConnection().sendMessage("banKick", name, reason.getReason(), !reason.isMute() + "");

            ReportCase reportCase;

            if((reportCase = SpigotPlugin.getSpigotPlugin().getReportManager().getReport(uuid.toString())) != null) {
                if(reportCase.isActive()) {
                    UUID byUUID = NameFetcher.getUUID(byName);
                    if(byUUID != null) {
                        if(byUUID.equals(UUID.fromString(reportCase.getCheckedByUUID()))) {
                            SpigotPlugin.getSpigotPlugin().getReportManager().agreeReport(
                                    NameFetcher.getName(UUID.fromString(reportCase.getCheckedByUUID())), reportCase);
                            return true;
                        }
                    }
                }
                SpigotPlugin.getSpigotPlugin().getReportManager().deleteReport(reportCase, "Banned from: " + byName);
            }
            return true;
        }
        return false;
    }

    public void unbanPlayer(String uuid, String byName) {
        if(isBanned(uuid)) {
            BanPlayer bp = getBannedPlayer(uuid);
            SpigotPlugin.getSpigotPlugin().getRecentActionsManager().addRecentAction(
                    new RecentUnbanAction(System.currentTimeMillis(), uuid, byName, bp.getUntilTime(), bp.getByName(), bp.getReason()));

            bannedPlayerCollection.deleteOne(Filters.eq("UUID", uuid));
        }
    }

    public boolean isBanned(String uuid) {
        return bannedPlayerCollection.find(Filters.eq("UUID", uuid)).first() != null;
    }

    public BanPlayer getBannedPlayer(String uuid) {
        Document doc = bannedPlayerCollection.find(Filters.eq("UUID", uuid)).first();
        if(doc != null){
            return BanPlayer.createFromMongoDocument(doc);
        }
        return null;
    }

    public ArrayList<BanPlayer> getBannendPlayerFromLastMonth(){
        ArrayList<BanPlayer> players = new ArrayList<>();
        for (Document document : bannedPlayerCollection.find(Filters.gte("banTime", System.currentTimeMillis() - 2592000000L))) {
            players.add(BanPlayer.createFromMongoDocument(document));
        }
        return players;
    }

    public ArrayList<BanReason> getBanReasons() {
        ArrayList<BanReason> reasons = new ArrayList<>();
        for (Document document : bannedReasonCollection.find()) {
            reasons.add(BanReason.createFromDoc(document));
        }
        return reasons;
    }

    public BanReason getBanReason(String reason) {
        Document doc = bannedReasonCollection.find(Filters.eq("Reason", reason.toUpperCase())).first();
        if(doc != null)
            return BanReason.createFromDoc(doc);
        return null;
    }

    public boolean existBanReason(String reason) {
        return bannedReasonCollection.find(Filters.eq("Reason", reason.toUpperCase())).first() != null;
    }

    public void removeBanReason(String reason){
        bannedReasonCollection.deleteOne(Filters.eq("Reason", reason.toUpperCase()));
    }

    public void addBanReason(String reason, long banTime, boolean isMute){
        bannedReasonCollection.insertOne(BanReason.createCustom(reason.toUpperCase(), banTime, isMute).toMongoDoc());
    }

}
