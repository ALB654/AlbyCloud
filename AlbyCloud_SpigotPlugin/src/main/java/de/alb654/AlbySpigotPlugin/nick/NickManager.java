package de.alb654.AlbySpigotPlugin.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.alb654.AlbySpigotPlugin.MongoDB.MongoManager;
import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.utils.GameProfileBuilder;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import net.minecraft.server.v1_12_R1.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 1.8.2020
 */

public class NickManager {

    private final SpigotPlugin plugin;
    private final Field nameField;

    public NickManager(SpigotPlugin plugin) {
        this.plugin = plugin;
        this.nameField = getField(GameProfile.class, "name");
    }

    public void nickPlayerInDB(String uuid, String name) {
        if(!isNickedInDB(uuid)) {
            MongoCollection<Document> collection = plugin.getMongoManager().getCollection(MongoManager.NICKLIST);
            collection.insertOne(new Document("UUID", uuid).append("NICK", name));
        }
    }

    public String getNickName(String uuid) {
        if(isNickedInDB(uuid)) {
            MongoCollection<Document> collection = plugin.getMongoManager().getCollection(MongoManager.NICKLIST);
            return collection.find(Filters.eq("UUID", uuid)).first().getString("NICK");
        }
        return null;
    }

    public boolean isNickedInDB(String uuid) {
        MongoCollection<Document> collection = plugin.getMongoManager().getCollection(MongoManager.NICKLIST);
        return collection.find(Filters.eq("UUID", uuid)).first() != null;
    }

    public void removeNickFromDB(String uuid) {
        if(isNickedInDB(uuid)) {
            MongoCollection<Document> collection = plugin.getMongoManager().getCollection(MongoManager.NICKLIST);
            collection.findOneAndDelete(Filters.eq("UUID", uuid));
        }
    }

    public void realizeNick(Player player) {
        String uuid = player.getUniqueId().toString();
        String name;

        if(isNickedInDB(uuid)) {
            name = getNickName(uuid);

            NickEvent event = new NickEvent(player, name);
            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled())
                return;

            name = event.getNickName();
        } else {
            Bukkit.getPluginManager().callEvent(new UnnickEvent(player));
            name = NameFetcher.getName(player.getUniqueId());
        }

        if(name == null)
            return;

        try {
            nameField.set(((CraftPlayer) player).getProfile(), name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        GameProfile profile = null;
        try {
            profile = GameProfileBuilder.fetch(NameFetcher.getUUID(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collection<Property> properties = profile.getProperties().get("textures");
        ((CraftPlayer) player).getProfile().getProperties().clear();
        Property property = properties.iterator().next();
        ((CraftPlayer) player).getProfile().getProperties().put("textures", property);

        PacketPlayOutPlayerInfo removeTabPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
        sendPacket(removeTabPacket);

        if(player.isOnline()) {
            PacketPlayOutPlayerInfo addTabPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());
            sendPacket(addTabPacket);
        }

        PacketPlayOutRespawn packet = new PacketPlayOutRespawn(((CraftPlayer) player).getWorld().getEnvironment().getId(), EnumDifficulty.getById(((CraftPlayer) player).getWorld().getDifficulty().getValue()), WorldType.getType(((CraftPlayer) player).getWorld().getWorldType().getName()), EnumGamemode.getById(((CraftPlayer) player).getGameMode().getValue()));
        sendPacket(player, packet);

        for(Player all : Bukkit.getOnlinePlayers()) {
            all.hidePlayer(plugin, player);
            all.showPlayer(plugin, player);
        }

        player.teleport(player.getLocation());

    }

    private static void sendPacket(Packet<?> packet) {
        for(Player all : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
        }
    }
    private static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    private Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
