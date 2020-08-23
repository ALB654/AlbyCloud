package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 14.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.ban.BanPlayer;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;

public class BannedPlayersGUI extends BloodInv {

    private final HashMap<Integer, ArrayList<BanPlayer>> sites;

    private int currentSite;

    public BannedPlayersGUI(Player player) {
        super(player, "§bGebannte Spieler §7- §b30 Tage", (short)6);

        this.sites = new HashMap<>();
        this.currentSite = 0;

        fillSites();
        init();
    }

    @Override
    public void init(Player p) {}

    private void init() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 45, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(4, GetItem.getItem(Material.SKULL_ITEM, "§bZuletzt gebannte Spieler", 1, "§7Hier siehst du alle gebannten Spieler der letzten 30 Tage", 3));

        setItem(48, GetItem.getItem(Material.FEATHER, "§cSpieler unbannen", 1, "§7Klicke um Spieler zu entbannen"));
        setItem(50, GetItem.getItem(Material.HOPPER, "§bSpieler suchen", 1, "§7Suche nach gebannten Spielern (Nicht nur des letzten Monats)"));

        setItem(53, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));

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
        switch(slot) {
            case 48:
                new UnbanPlayersGUI(getPlayer());
                break;
            case 50:
                SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Spielernamen", "§7in den Chat", (player, msg) -> {

                    UUID uuid = NameFetcher.getUUID(msg);
                    if(uuid != null) {
                        BanPlayer banPlayer = SpigotPlugin.getSpigotPlugin().getBanManager().getBannedPlayer(uuid.toString());
                        if(banPlayer != null)
                            new BannedPlayerInfoGUI(getPlayer(), banPlayer);
                        else {
                            getPlayer().sendMessage(CloudApi.Bprefix + "§cDieser Spieler ist nicht gebannt!");
                            getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        }
                    } else {
                        getPlayer().sendMessage(CloudApi.Bprefix + "§cDieser Spieler existiert nicht!");
                        getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                    }
                }, (player) -> new BannedPlayersGUI(getPlayer()));
                break;
            case 53:
                new PlayerBanMenuGUI(getPlayer());
                break;
        }

        if(sites.size() > 1) {
            if (slot == 46) {
                currentSite--;
                if (currentSite < 0)
                    currentSite = sites.size() - 1;

                showCurrentSite();
            } else if (slot == 52) {
                currentSite++;
                if (currentSite >= sites.size())
                    currentSite = 0;

                showCurrentSite();
            }
        }
        return true;
    }
}
