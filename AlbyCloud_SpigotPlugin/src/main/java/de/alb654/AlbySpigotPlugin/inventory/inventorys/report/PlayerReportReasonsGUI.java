package de.alb654.AlbySpigotPlugin.inventory.inventorys.report;/*
 * Copyright (c)
 * Created by Aaron Bernert on 26.8.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.report.ReportCase;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerReportReasonsGUI extends BloodInv {

    private ReportCase reportCase;

    public PlayerReportReasonsGUI(Player player, ReportCase reportCase) {
        super(player, "§2Reportgründe §7- " + reportCase.getName(), (short) 5);

        this.reportCase = reportCase;
        initS();
    }

    @Override
    public void init(Player p) {}

    int slot = 9;
    private void initS() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 36, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }


        for(ReportCase.UUIDReason reason : reportCase.getReporterAndReasons()) {
            setItem(slot, GetItem.getItem(Material.SKULL_ITEM, "§2" + NameFetcher.getName(UUID.fromString(reason.getUuid())), 1,
                    "§7Grund: §2" + reason.getReason()));

            new BukkitRunnable() {
                int s = slot;
                @Override
                public void run() {
                    setItem(slot, GetItem.getHead(NameFetcher.getName(UUID.fromString(reason.getUuid())), "§2" + NameFetcher.getName(UUID.fromString(reason.getUuid())),
                            "§7Grund: §2" + reason.getReason()));
                }
            }.runTask(SpigotPlugin.getSpigotPlugin());
        }

        setItem(4, GetItem.getItem(Material.NAME_TAG, "§2Reportgründe", 1, "§7Hier werden alle Reporter mit den Gründen angezeigt"));
        setItem(40, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(slot == 40)
            new ReportGUI(getPlayer(), reportCase);
        return true;
    }
}
