package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 14.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.ban.BanPlayer;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;

public class UnbanPlayersGUI extends BloodInv {

    private final HashMap<Integer, ArrayList<BanPlayer>> sites;

    private int currentSite;

    public UnbanPlayersGUI(Player player) {
        super(player, "§bGebannte Spieler §7- §cUnbannen", (short)6);

        this.sites = new HashMap<>();
        this.currentSite = 0;

        fillSites();
        init();
    }

    @Override
    public void init(Player p) {}

    private void init() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
            setItem(i + 45, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
        }

        setItem(4, GetItem.getItem(Material.SKULL_ITEM, "§bZuletzt gebannte Spieler", 1, "§7Klicke auf Spieler, um sie zu unbannen", 3));
        setItem(49, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cAbbrechen", 1));

        if(sites.size() > 1) {
            setItem(46, GetItem.getItem(Material.ARROW, "§bSeite zurück", 1));
            setItem(52, GetItem.getItem(Material.ARROW, "§bSeite vor", 1));
        }
    }

    private void fillSites() {
        ArrayList<BanPlayer> bannedPlayer = SpigotPlugin.getSpigotPlugin().getBanManager().getBannendPlayerFromLastMonth();
        for(int site = 0; site <= bannedPlayer.size() / 36; site++) {
            sites.put(site, new ArrayList<>(bannedPlayer.subList(site * 36, Math.min((site * 36) + 36, bannedPlayer.size()))));
        }

        showCurrentSite();
    }

    private int currentFillSlot = 0;

    private void showCurrentSite() {
        List<BanPlayer> bannedPlayers = sites.get(currentSite);
        if(bannedPlayers != null && !bannedPlayers.isEmpty()){
            for(int i = 0; i < 36; i++)
                setItem(i + 9, new ItemStack(Material.AIR));

            currentFillSlot = 0;
            while (currentFillSlot < bannedPlayers.size()) {
                BanPlayer bannedPlayer = bannedPlayers.get(currentFillSlot);
                if(bannedPlayer != null) {
                    new BukkitRunnable() {
                        final int slot = currentFillSlot;
                        final int showSite = currentSite;
                        @Override
                        public void run() {
                            if(showSite == currentSite)
                                setItem(slot + 9, GetItem.getHead(bannedPlayer.getName(), "§c" + bannedPlayer.getName() + " §bgebannt",
                                        new ArrayList<>(Arrays.asList("§7von: §b" + bannedPlayer.getByName(),
                                                "§7Grund: §b" + bannedPlayer.getReason(),
                                                "§7Mute: §b" + (bannedPlayer.isMute() ? "Ja" : "Nein"),
                                                "§7bis: §b" + (bannedPlayer.getUntilTime() == -1 ? "PERMANENT" : new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(bannedPlayer.getUntilTime()))),
                                                "§7am: §b" + new SimpleDateFormat("dd.MM.yyyy").format(new Date(bannedPlayer.getBanTime())),
                                                "§7um: §b" + new SimpleDateFormat("HH:mm:ss").format(new Date(bannedPlayer.getBanTime()))))));
                        }
                    }.runTask(SpigotPlugin.getSpigotPlugin());
                    setItem(currentFillSlot + 9, GetItem.getItem(Material.SKULL_ITEM, "§c" + bannedPlayer.getName() + " §bgebannt", 1,
                            new ArrayList<>(Arrays.asList("§7von: §b" + bannedPlayer.getByName(),
                                    "§7Grund: §b" + bannedPlayer.getReason(),
                                    "§7Mute: §b" + (bannedPlayer.isMute() ? "Ja" : "Nein"),
                                    "§7bis: §b" + (bannedPlayer.getUntilTime() == -1 ? "PERMANENT" : new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(bannedPlayer.getUntilTime()))),
                                    "§7am: §b" + new SimpleDateFormat("dd.MM.yyyy").format(new Date(bannedPlayer.getBanTime())),
                                    "§7um: §b" + new SimpleDateFormat("HH:mm:ss").format(new Date(bannedPlayer.getBanTime())))), 3));
                }
                currentFillSlot++;
            }
        }
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(slot == 49) {
            new BannedPlayersGUI(getPlayer());
        }

        if(sites.size() > 1) {
            if(slot == 46) {
                currentSite--;
                if(currentSite < 0)
                    currentSite = sites.size() - 1;

                showCurrentSite();
            } else if(slot == 52){
                currentSite++;
                if(currentSite >= sites.size())
                    currentSite = 0;

                showCurrentSite();
            }
        }

        if(slot >= 9 && slot < 45) {
            if(getInv().getItem(slot) == null || getInv().getItem(slot).getType() == Material.AIR)
                return true;

            BanPlayer bannedPlayer = sites.get(currentSite).get(slot - 9);
            if(bannedPlayer != null){

                SpigotPlugin.getSpigotPlugin().getBanManager().unbanPlayer(bannedPlayer.getUuid(), getPlayer().getName());
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 1f);

                setItem(slot, new ItemStack(Material.AIR));
                sites.get(currentSite).set(slot - 9, null);
            }
        }
        return true;
    }
}
