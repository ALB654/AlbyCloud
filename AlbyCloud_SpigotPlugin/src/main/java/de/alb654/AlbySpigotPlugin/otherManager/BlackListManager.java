package de.alb654.AlbySpigotPlugin.otherManager;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 18.7.2020
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import org.bson.Document;

import java.util.ArrayList;

public class BlackListManager {

    private final MongoCollection<Document> blacklist;

    public BlackListManager(MongoManager mongoManager) {
        this.blacklist = mongoManager.getCollection(MongoManager.BLACKLIST);
    }

    public void removeBlacklistedWord(String word) {
        blacklist.findOneAndDelete(Filters.eq("Word", word.toLowerCase()));
    }

    public void addBlacklistesWord(String word) {
        blacklist.insertOne(new Document("Word", word.toLowerCase()));
    }

    public boolean isBlacklisted(String word) {
        return blacklist.find(Filters.eq("Word", word.toLowerCase())).first() != null;
    }

    public boolean containsBlacklistedWord(String[] words) {
        for(String s : words)
            if(isBlacklisted(s))
                return true;
        return false;
    }

    public boolean containsBlacklistedWord(String phrase) {
        return containsBlacklistedWord(phrase.split("[- \\\\._/%,+#';:?=)(&$§\"!<>}\\]\\[{*~|]+"));
    }

    public boolean replaceNumbersAndcheckIfContainsWord(String phrase) {
        return containsBlacklistedWord(phrase.replace("3", "e").replace("4", "a").replace("5", "s").replace("1", "i").replace("0", "o"));
    }
}
