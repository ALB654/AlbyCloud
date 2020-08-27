package de.alb654.AlbyBungeePlugin.mongo;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 1.7.2020
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoManager {

    public static final String PERMISSIONS = "Permissions";
    public static final String BLOODPLAYER = "BloodPlayer";
    public static final String BANNEDPLAYER = "BannedPlayer";
    public static final String BANREASONS = "BanReasons";
    public static final String RECENTCOLLECTION = "RecentCollection";
    public static final String REPORTS = "Reports";

    private final MongoClient client;
    private MongoDatabase database;

    public MongoManager(String uri) {
        MongoClientURI clientURI = new MongoClientURI(uri);
        client = new MongoClient(clientURI);
    }

    public void connectToDatabase(String name) {
        database = client.getDatabase(name);
    }

    public MongoCollection<Document> getCollection(String name) {
        return database != null ? database.getCollection(name) : null;
    }

}
