package de.alb654.AlbySpigotPlugin.permissions;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 1.7.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;

public class PermissionGroup {

    private final String name;

    public PermissionGroup(String name) {
        this.name = name;
    }

    public boolean exists() {
        return existsGroup(name);
    }

    @SuppressWarnings("unchecked")
    public void addPlayer(String uuid){
        MongoCollection<Document> permCollection = getPermCollection();
        Document group = getPermDocument(permCollection);

        if(group == null) {
            group = new Document()
                    .append("GroupName", name)
                    .append("Spigot_Perms", new ArrayList<>())
                    .append("Bungee_Perms", new ArrayList<>())
                    .append("Player", new ArrayList<>(Collections.singletonList(uuid)));
            permCollection.insertOne(group);
        } else {
            ArrayList<String> oldPlayer = (ArrayList<String>) group.get("Player");
            oldPlayer = new ArrayList<>(oldPlayer);

            if(!oldPlayer.contains(uuid)) {
                oldPlayer.add(uuid);
                permCollection.updateOne(Filters.eq("GroupName", name), Updates.set("Player", oldPlayer));
            }
        }
    }

    public void removePlayer(String uuid) {
        MongoCollection<Document> permCollection = getPermCollection();
        Document group = getPermDocument(permCollection);

        if(group != null) {
            ArrayList<String> oldPlayer = (ArrayList<String>) group.get("Player");
            oldPlayer = new ArrayList<>(oldPlayer);

            if(oldPlayer.contains(uuid)) {
                oldPlayer.remove(uuid);

                permCollection.updateOne(Filters.eq("GroupName", name), Updates.set("Player", oldPlayer));
            }
        }
    }

    public ArrayList<String> getPlayerUUIDs() {
        Document perms = getPermDocument(getPermCollection());
        if(perms == null)
            return new ArrayList<>();

        return (ArrayList<String>) perms.get("Player");
    }

    @SuppressWarnings("unchecked")
    public void addSpigotPerm(String permission) {
        MongoCollection<Document> permCollection = getPermCollection();
        Document perms = getPermDocument(permCollection);

        if(perms == null) {
            perms = new Document()
                    .append("GroupName", name)
                    .append("Spigot_Perms", new ArrayList<>(Collections.singletonList(permission)))
                    .append("Bungee_Perms", new ArrayList<>())
                    .append("Player", new ArrayList<>());
            permCollection.insertOne(perms);
        } else {
            ArrayList<String> oldPerms = (ArrayList<String>) perms.get("Spigot_Perms");
            oldPerms = new ArrayList<>(oldPerms);

            if(!oldPerms.contains(permission)) {
                oldPerms.add(permission);
                permCollection.updateOne(Filters.eq("GroupName", name), Updates.set("Spigot_Perms", oldPerms));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void removeSpigotPerm(String permission) {
        MongoCollection<Document> permCollection = getPermCollection();
        Document perms = getPermDocument(permCollection);

        if(perms != null) {
            ArrayList<String> oldPerms = (ArrayList<String>) perms.get("Spigot_Perms");
            oldPerms = new ArrayList<>(oldPerms);

            if(oldPerms.contains(permission)) {
                oldPerms.remove(permission);
                permCollection.updateOne(Filters.eq("GroupName", name), Updates.set("Spigot_Perms", oldPerms));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void addBungeePerm(String permission) {
        MongoCollection<Document> permCollection = getPermCollection();
        Document perms = getPermDocument(permCollection);

        if(perms == null) {
            perms = new Document()
                    .append("GroupName", name)
                    .append("Spigot_Perms", new ArrayList<>())
                    .append("Bungee_Perms", new ArrayList<>(Collections.singletonList(permission)))
                    .append("Player", new ArrayList<>());
            permCollection.insertOne(perms);
        } else {
            ArrayList<String> oldPerms = (ArrayList<String>) perms.get("Bungee_Perms");
            oldPerms = new ArrayList<>(oldPerms);

            if(!oldPerms.contains(permission)) {
                oldPerms.add(permission);
                permCollection.updateOne(Filters.eq("GroupName", name), Updates.set("Bungee_Perms", oldPerms));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void removeBungeePerm(String permission) {
        MongoCollection<Document> permCollection = getPermCollection();
        Document perms = getPermDocument(permCollection);

        if(perms != null) {
            ArrayList<String> oldPerms = (ArrayList<String>) perms.get("Bungee_Perms");
            oldPerms = new ArrayList<>(oldPerms);

            if(oldPerms.contains(permission)) {
                oldPerms.remove(permission);
                permCollection.updateOne(Filters.eq("GroupName", name), Updates.set("Bungee_Perms", oldPerms));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getSpigotPerms() {
        Document perms = getPermDocument(getPermCollection());
        if(perms == null)
            return new ArrayList<>();

        return (ArrayList<String>) perms.get("Spigot_Perms");
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getBungeePerms() {
        Document perms = getPermDocument(getPermCollection());
        if(perms == null)
            return new ArrayList<>();

        return (ArrayList<String>) perms.get("Bungee_Perms");
    }

    private MongoCollection<Document> getPermCollection() {
        return SpigotPlugin.getSpigotPlugin().getMongoManager().getCollection(MongoManager.PERMISSIONS);
    }

    private Document getPermDocument(MongoCollection<Document> permCollection) {
        return permCollection.find(new Document("GroupName", name)).first();
    }

    public static ArrayList<String> getAllGroupNames(){
        MongoCollection<Document> collection = SpigotPlugin.getSpigotPlugin().getMongoManager().getCollection(MongoManager.PERMISSIONS);
        Document groupList = collection.find(Filters.eq("GroupList", "this")).first();

        ArrayList<String> groups = new ArrayList<>();
        if(groupList != null)
            groupList.forEach((key, value) -> {
                if(value.equals("true"))
                    groups.add(key);
            });

        return groups;
    }

    public static void removeGroup(String name) {
        if(existsGroup(name)) {
            MongoCollection<Document> collection = SpigotPlugin.getSpigotPlugin().getMongoManager().getCollection(MongoManager.PERMISSIONS);
            collection.updateOne(Filters.eq("GroupList", "this"), Updates.set(name, "false"));
        }
    }

    public static void addGroup(String name) {
        if(!existsGroup(name)) {
            MongoCollection<Document> collection = SpigotPlugin.getSpigotPlugin().getMongoManager().getCollection(MongoManager.PERMISSIONS);
            Document groupList = collection.find(Filters.eq("GroupList", "this")).first();
            if(groupList == null) {
                groupList = new Document("GroupList", "this").append(name, "true");
                collection.insertOne(groupList);
            } else {
                collection.updateOne(Filters.eq("GroupList", "this"), Updates.set(name, "true"));
            }
        }
    }

    public static boolean existsGroup(String name) {
        MongoCollection<Document> collection = SpigotPlugin.getSpigotPlugin().getMongoManager().getCollection(MongoManager.PERMISSIONS);
        Document groupList = collection.find(Filters.eq("GroupList", "this")).first();
        return groupList != null && groupList.containsKey(name) && groupList.get(name).equals("true");
    }
}
