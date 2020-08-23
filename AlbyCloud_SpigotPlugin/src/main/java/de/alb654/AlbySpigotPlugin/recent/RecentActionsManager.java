package de.alb654.AlbySpigotPlugin.recent;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import org.bson.Document;

import java.util.ArrayList;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

public class RecentActionsManager {

    private final MongoCollection<Document> recentCollection;

    public RecentActionsManager(MongoManager mongoManager) {
        this.recentCollection = mongoManager.getCollection(MongoManager.RECENTCOLLECTION);
    }

    public void addRecentAction(RecentAction recentAction) {
        recentCollection.insertOne(recentAction.toMongoDoc());
        while(getSavedActionAmount(recentAction.type) >= 10){
            removeLastAction(recentAction.type);
        }
    }

    private void removeLastAction(RecentAction.ActionType actionType) {
        Document last = recentCollection.find(Filters.eq("type", actionType.name())).sort(Sorts.ascending("actionTime")).first();
        if(last != null)
            recentCollection.deleteOne(last);
    }

    private int getSavedActionAmount(RecentAction.ActionType actionType) {
        return getRecentActions(actionType).size();
    }

    public ArrayList<RecentAction> getRecentActions(RecentAction.ActionType actionType) {
        ArrayList<RecentAction> actions = new ArrayList<>();
        for(Document doc : recentCollection.find()) {
            RecentAction.ActionType type = RecentAction.ActionType.valueOf(doc.getString("type"));
            switch (type) {
                case BAN:
                    RecentBanAction banAction = RecentBanAction.createFromMongoDoc(doc);
                    if(actionType == banAction.type)
                        actions.add(banAction);
                    break;
                case UNBAN:
                    RecentUnbanAction unbanAction = RecentUnbanAction.createFromMongoDoc(doc);
                    if(actionType == unbanAction.type)
                        actions.add(unbanAction);
                    break;
                case REPORT:
                    RecentReportAction reportAction = RecentReportAction.createFromMongoDoc(doc);
                    if(actionType == reportAction.type)
                        actions.add(reportAction);
                    break;
            }
        }
        actions.sort((recentAction, otherAction) -> recentAction.getActionTime() > otherAction.getActionTime() ? -1 : 1);
        return actions;
    }



}
