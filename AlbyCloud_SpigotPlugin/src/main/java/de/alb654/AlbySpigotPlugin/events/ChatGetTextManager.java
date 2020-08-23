package de.alb654.AlbySpigotPlugin.events;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.utils.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatGetTextManager implements Listener {

    private final ArrayList<TextChatGetter> waitings = new ArrayList<>();

    {
        startUpdating();
    }

    public void addGetText(Player player, String title, String subtitle, BiConsumer<Player, String> onTextEnter, Consumer<Player> onCancel) {
        if(!isGettingText(player.getUniqueId().toString())) {
            waitings.add(new TextChatGetter(player, title, subtitle, onTextEnter, onCancel));
            for (int i = 0; i < 3; i++)
                player.sendMessage(" ");
            player.sendMessage(CloudApi.Bprefix + "§7Schreibe in den Chat. Tippe §cCancel§7 um abzubrechen.");
            player.sendMessage(" ");
            player.getOpenInventory().close();
        }
    }

    private void startUpdating(){
        new BukkitRunnable() {
            @Override
            public void run() {
                synchronized (waitings) {
                    ArrayList<TextChatGetter> remove = new ArrayList<>();
                    for (TextChatGetter chatGetter : waitings) {
                        if (chatGetter.isActive && chatGetter.getPlayer().isOnline()) {
                            Title.sendTitle(chatGetter.getPlayer(), 0, 50, 20, chatGetter.getTitle(), chatGetter.getSubtitle());

                        } else
                            remove.add(chatGetter);
                    }
                    waitings.removeAll(remove);
                }
            }
        }.runTaskTimer(SpigotPlugin.getSpigotPlugin(), 20 * 10, 15);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        synchronized (waitings) {
            for (TextChatGetter chatGetter : waitings) {
                if (chatGetter.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId()) && chatGetter.isActive()) {
                    if (e.getMessage().equalsIgnoreCase("Cancel"))
                        chatGetter.cancel();
                    else
                        chatGetter.text(e.getMessage());
                    e.setCancelled(true);
                }
            }
        }
    }

    public boolean isGettingText(String playerUUID) {
        for(TextChatGetter chatGetter : waitings) {
            if(chatGetter.getPlayer().getUniqueId().toString().equalsIgnoreCase(playerUUID)) {
                return true;
            }
        }
        return false;
    }

    private class TextChatGetter {

        private final Player player;
        private String title, subtitle;
        private final BiConsumer<Player, String> onTextEnter;
        private final Consumer<Player> onCancel;

        private boolean isActive;

        private TextChatGetter(Player player, String title, String subtitle, BiConsumer<Player, String> onTextEnter, Consumer<Player> onCancel) {
            this.player = player;
            this.title = title;
            this.subtitle = subtitle;
            this.onTextEnter = onTextEnter;
            this.onCancel = onCancel;
            this.isActive = true;
        }

        public void text(String msg) {
            isActive = false;
            onTextEnter.accept(player, msg);
        }

        public void cancel() {
            isActive = false;
            onCancel.accept(player);
        }

        public boolean isActive() {
            return isActive;
        }

        public Player getPlayer() {
            return player;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getTitle() {
            return title;
        }
    }
}
