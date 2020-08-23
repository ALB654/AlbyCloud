package de.alb654.AlbySpigotPlugin.recent;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

import org.bson.Document;

public class RecentBanAction extends RecentAction {

    public RecentBanAction(long actionTime, String suspectUUID) {
        super(actionTime, suspectUUID, ActionType.BAN);
    }

    public static RecentBanAction createFromMongoDoc(Document doc) {
        return new RecentBanAction(doc.getLong("actionTime"), doc.getString("suspectUUID"));
    }
}
