package de.alb654.AlbySpigotPlugin.api;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 30.6.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.permissions.PermissionGroup;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;

public class BloodPlayer {

    private final String name;
    private final String uuid;

    public BloodPlayer(String name) {
        this.name = name;
        this.uuid = NameFetcher.getUUID(name) == null ? "no" : NameFetcher.getUUID(name).toString();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getPermissionGroups(){
        return (ArrayList<String>) getStat(Stat.PERM_GROUP);
    }

    public void addPermissionGroup(String name) {
        PermissionGroup pg = new PermissionGroup(name);
        if(pg.exists()) {
            ArrayList<String> groups = getPermissionGroups();
            groups.add(name);
            setStat(Stat.PERM_GROUP, groups);
            pg.addPlayer(uuid);
        }
    }

    public void removePermissionGroup(String name) {
        PermissionGroup pg = new PermissionGroup(name);
        if(pg.exists()) {
            pg.removePlayer(uuid);
        }
        ArrayList<String> groups = getPermissionGroups();
        groups.remove(name);
        setStat(Stat.PERM_GROUP, groups);
    }

    public void setStat(Stat stat, Object value) {
        if(exists()) {
            MongoCollection<Document> mongoCollection = getCollection();
            mongoCollection.updateOne(Filters.eq("UUID", uuid), Updates.set(stat.name(), value));
        }
    }

    public Object getStat(Stat stat) {
        Document doc = getDocument();
        return doc == null ? null : doc.get(stat.name());
    }

    public enum Stat {
        PERM_GROUP
    }

    private MongoCollection<Document> getCollection() {
        return SpigotPlugin.getSpigotPlugin().getMongoManager().getCollection(MongoManager.BLOODPLAYER);
    }

    private Document getDocument() {
        return getCollection().find(new Document("UUID", uuid)).first();
    }

    public boolean exists() {
        return getDocument() != null;
    }

    public void createDefault() {
        Document doc = new Document("Name", name)
                .append("UUID", uuid)
                .append("PERM_GROUP", new ArrayList<>(Collections.singletonList("Default")));
        getCollection().insertOne(doc);
    }

}
