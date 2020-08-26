package de.alb654.AlbySpigotPlugin.inventory.inventorys.report;/*
 * Copyright (c)
 * Created by Aaron Bernert on 25.8.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.report.ReportCase;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ReportGUI extends BloodInv {

    private final ReportCase rCase;

    public ReportGUI(Player player, ReportCase rCase) {
        super(player, "§2Report §7- §2" + rCase.getName(), (short) 3);

        this.rCase = rCase;
        initS();
    }

    @Override
    public void init(Player p) {}

    private void initS() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(4, GetItem.getItem(Material.NAME_TAG, "§2Report §7- §2" + rCase.getName(), 1));
        setItem(10, GetItem.getItem(Material.SKULL_ITEM, "§2" + rCase.getName(), 1));

        new BukkitRunnable() {
            @Override
            public void run() {
                setItem(10, GetItem.getHead(rCase.getName(), "§2" + rCase.getName()));
            }
        }.runTask(SpigotPlugin.getSpigotPlugin());

        setItem(16, GetItem.getItemWithoutAttribs(Material.MAP, "§2Gründe", 1, "§7Siehe dir alle Reporter + Gründe an",
                "§7Anzahl: §2" + rCase.getReporterAndReasons().size()));

        if(!rCase.isActive()) {
            setItem(21, GetItem.getItem(Material.EMERALD_BLOCK, "§aAnnehmen", 1, "§7Du hast 15min Zeit um dich um den Fall zu kümmern"));
            setItem(23, GetItem.getItem(Material.REDSTONE_BLOCK, "§cLöschen", 1));
        }
        setItem(26, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        switch (slot) {
            case 16:
                new PlayerReportReasonsGUI(getPlayer(), rCase);
                break;
            case 21:
                if(!rCase.isActive()) {
                    SpigotPlugin.getSpigotPlugin().getReportManager().acceptReport(getPlayer().getUniqueId().toString(), rCase.getUuid());
                    getPlayer().closeInventory();
                    getPlayer().sendMessage(CloudApi.Bprefix + "§7Du hast den Report von §2" + rCase.getName() + "§7 angenommen.");
                    getPlayer().sendMessage(CloudApi.Bprefix + "§7Du hast §215min §7Zeit um ihn zu bearbeiten.");
                    getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                }
                break;
            case 23:
                if(rCase.isActive()) {
                    SpigotPlugin.getSpigotPlugin().getReportManager().deleteReport(rCase, NameFetcher.getName(getPlayer().getUniqueId()));
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                    new ReportsGUI(getPlayer());
                }
                break;
            case 26:
                new ReportsGUI(getPlayer());
                break;
        }
        return true;
    }
}
