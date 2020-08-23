package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BanReasonCreateGUI extends BloodInv {

    private String reason;
    private boolean mute;
    private long time;

    public BanReasonCreateGUI(Player player) {
        super(player, "§bBangrund erstellen", (short) 3);

        init();
    }

    public BanReasonCreateGUI(Player player, String reason, boolean mute, long time) {
        this(player);

        setReason(reason);
        setMute(mute);
        setTime(time);
    }

    @Override
    public void init(Player p) {}

    private void init() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(4, GetItem.getItemWithoutAttribs(Material.MAP, "§bBangrund hinzufügen", 1, "§7Du bist dabei einen Bangrund hinzuzufügen"));

        setReason(" - ");
        setMute(false);
        setTime(-1);

        setItem(22, GetItem.getItem(Material.EMERALD_BLOCK, "§aHinzufügen", 1, "§7Klicke, um den Grund hinzuzufügen"));
        setItem(26, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cAbbrechen", 1));
    }

    private void setReason(String reason) {
        this.reason = reason;
        setItem(11, GetItem.getItem(Material.PAPER, "§bGrund§7: §c" + reason, 1, "§7Klicke zum ändern"));
    }

    private void setMute(boolean mute) {
        this.mute = mute;
        if(mute)
            setItem(13, GetItem.getItem(Material.PAINTING, "§bMute§7: §cJa", 1, "§7Klicke zum ändern"));
        else
            setItem(13, GetItem.getItemWithoutAttribs(Material.STONE_SWORD, "§bMute§7: §cNein", 1, "§7Klicke zum ändern"));
    }

    private void setTime(long millis) {
        this.time = millis;
        setItem(15, GetItem.getItem(Material.WATCH, "§bZeit§7: §c" + Utils.toTimeString(millis), 1, "§7Klicke zum ändern"));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {

        switch (slot) {
            case 11:
                SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Grund", "§7in den Chat",
                        (player, msg) -> {
                            new BanReasonCreateGUI(player, msg, mute, time);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                        }, (player) -> {
                            new BanReasonCreateGUI(player, reason, mute, time);
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        });
                break;
            case 13:
                setMute(!mute);
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 1f);
                break;
            case 15:
                SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Zeit in den Chat", "§7Format: Xd Xh Xm Xs (Schreibe Permanet für Permaban)",
                        (player, msg) -> {
                            long millis = Utils.timeStringToMillis(msg);
                            if(millis <= -2) {
                                player.sendMessage(CloudApi.Bprefix + "§cFalsches Format angegeben!");
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                            } else {
                                new BanReasonCreateGUI(player, reason, mute, millis);
                            }
                        }, (player -> {
                            new BanReasonCreateGUI(player, reason, mute, time);
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        }));
                break;
            case 22:
                if(!reason.equalsIgnoreCase(" - ") && SpigotPlugin.getSpigotPlugin().getBanManager().getBanReason(reason) == null) {
                    SpigotPlugin.getSpigotPlugin().getBanManager().addBanReason(reason, time, mute);
                    getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    new BanReasonListGUI(getPlayer());
                } else {
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                }
                break;
            case 26:
                new BanReasonListGUI(getPlayer());
                break;
        }

        return true;
    }
}
