package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.ban.BanReason;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.Title;
import de.alb654.AlbySpigotPlugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayerBanGUI extends BloodInv {

    private final String suspect;
    private final ArrayList<BanReason> banReasons;

    private int currentBanReasonInt = 0;

    private String currentReason;
    private long currentBanTime;
    private boolean currentlyMuting;

    public PlayerBanGUI(Player player, String suspect) {
        super(player, "§bSpieler bannen", (short) 3);
        this.suspect = suspect;
        this.banReasons = SpigotPlugin.getSpigotPlugin().getBanManager().getBanReasons();

        init();
    }

    public PlayerBanGUI(Player player, String suspect, String setReason, long setBanTime, boolean setIsMuted) {
        this(player, suspect);

        this.currentReason = setReason;
        this.currentBanTime = setBanTime;
        this.currentlyMuting = setIsMuted;
        setCurrentBanParameters();
    }

    @Override
    public void init(Player p) {}

    private void init() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(4, GetItem.getItemWithoutAttribs(Material.DIAMOND_SWORD, "§b" + suspect + " bannen", 1, "§7Du bist dabei " + suspect + " zu bannen"));
        setItem(10, GetItem.getHead(suspect, "§b" + suspect, Collections.singletonList("§7Diesen Spieler willst du Bannen")));

        setCurrentBanReason();

        setItem(22, GetItem.getItem(Material.EMERALD_BLOCK, "§aBANNEN", 1, "§7Klicke um den Spieler zu bannen"));
        setItem(26, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cAbbrechen", 1, "§7Kehre zum Menu zurück"));
    }

    private void setCurrentBanReason() {
        BanReason banReason;

        if(banReasons.size() <= currentBanReasonInt)
            banReason = BanReason.createCustom("Undefined", 1000, false);
        else
            banReason = banReasons.get(currentBanReasonInt);

        setItem(12, GetItem.getItemWithoutAttribs(Material.MAP,
                "§bBanreason§7: §c" + banReason.getReason(), 1,
                new ArrayList<>(Arrays.asList("§7Wähle einen Bangrund aus",
                        "§7Klicke zum ändern"))));

        currentReason = banReason.getReason();
        currentBanTime = banReason.getBanTime();
        currentlyMuting = banReason.isMute();

        setCurrentBanParameters();
    }

    private void setCurrentBanParameters() {

        setItem(14, GetItem.getItem(Material.PAPER, "§bGrund§7: §c" + currentReason, 1,
                "§7Klicke um einen Customgrund anzugeben"));

        setItem(15, GetItem.getItem(Material.WATCH, "§bZeit§7: §c" + Utils.toTimeString(currentBanTime), 1,
                "§7Klicke um eine Customzeit anzugeben"));

        if(currentlyMuting)
            setItem(16, GetItem.getItem(Material.PAINTING, "§bMute§7: §cJa", 1,
                    "§7Klicke um den Spieler zu bannen"));
        else
            setItem(16, GetItem.getItemWithoutAttribs(Material.STONE_SWORD, "§bMute§7: §cNein", 1,
                    "§7Klicke um den Spieler nur zu muten"));

    }

    @Override
    public boolean onClick(int slot, ClickType type) {

        switch (slot) {
            case 12:
                currentBanReasonInt++;
                if(currentBanReasonInt >= banReasons.size())
                    currentBanReasonInt = 0;
                setCurrentBanReason();
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 1f);
                break;
            case 14:
                SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Grund", "§7in den Chat",
                        (player, msg) -> {
                            new PlayerBanGUI(player, suspect, msg, currentBanTime, currentlyMuting);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                        }, (player) ->{
                                new PlayerBanGUI(player, suspect, currentReason, currentBanTime, currentlyMuting);
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        });
                break;
            case 15:
                SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Zeit in den Chat", "§7Format: Xd Xh Xm Xs (Schreibe Permanet für Permaban)",
                        (player, msg) -> {
                            long millis = Utils.timeStringToMillis(msg);
                            if(millis <= -2) {
                                player.sendMessage(CloudApi.Bprefix + "§cFalsches Format angegeben!");
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                            } else {
                                new PlayerBanGUI(player, suspect, currentReason, millis, currentlyMuting);
                            }
                        }, (player -> {
                            new PlayerBanGUI(player, suspect, currentReason, currentBanTime, currentlyMuting);
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        }));
                break;
            case 16:
                currentlyMuting = !currentlyMuting;
                setCurrentBanParameters();
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 1f);
                break;
            case 22:
                if(SpigotPlugin.getSpigotPlugin().getBanManager().banPlayer(suspect, BanReason.createCustom(currentReason, currentBanTime, currentlyMuting), getPlayer().getName())) {
                    getPlayer().closeInventory();
                    Title.sendTitle(getPlayer(), 2, 60, 2, "§6" + suspect + " §c" + (currentlyMuting ? "gemutet!" : "gebannt!"), "§7");
                    getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                } else {
                    getPlayer().sendMessage(CloudApi.Bprefix + "§cDieser Spieler ist bereits gebannt!");
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                }

                break;
            case 26:
                new PlayerBanMenuGUI(getPlayer());
                break;
        }

        return true;
    }
}
