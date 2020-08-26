package de.alb654.AlbySpigotPlugin.inventory.inventorys.report;/*
 * Copyright (c)
 * Created by Aaron Bernert on 24.8.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.recent.RecentAction;
import de.alb654.AlbySpigotPlugin.recent.RecentReportAction;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.UUID;

public class LatestClosedReportsGUI extends BloodInv {

    public LatestClosedReportsGUI(Player player) {
        super(player, "§2Letzte Reports", (short) 3);
    }

    @Override
    public void init(Player p) {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        ArrayList<RecentAction> recentReports = SpigotPlugin.getSpigotPlugin().getRecentActionsManager().getRecentActions(RecentAction.ActionType.REPORT);
        recentReports.sort((a, b) -> a.getActionTime() >= b.getActionTime() ? 1 : -1);
        for(int i = 0 ; i < 9 && i < recentReports.size(); i++) {
            RecentReportAction reportAction = (RecentReportAction) recentReports.get(i);
            if (reportAction.isBanned())
                setItem(i + 9, GetItem.getItemWithoutAttribs(Material.DIAMOND_SWORD, "§2" + NameFetcher.getName(UUID.fromString(reportAction.getSuspectUUID())), 1,
                        "§7Anzahl Reports: §2" + reportAction.getReportsReasons().size()));
            else if (reportAction.isDeleted())
                setItem(i + 9, GetItem.getItem(Material.SULPHUR, "§2" + NameFetcher.getName(UUID.fromString(reportAction.getSuspectUUID())), 1,
                        "§7Anzahl Reports: §2" + reportAction.getReportsReasons().size(), "§7Gelöscht von: §2" + reportAction.getProcessedBy()));
            else
                setItem(i + 9, GetItem.getItem(Material.WOOL, "§2" + NameFetcher.getName(UUID.fromString(reportAction.getSuspectUUID())), 1,
                        "§7Anzahl Reports: §2" + reportAction.getReportsReasons().size(), 14));

        }

        setItem(4, GetItem.getItemWithoutAttribs(Material.MAP, "§2Letzte Reports", 1, "§7Alle zuletzt bearbeiteten Reports"));

        setItem(26, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(slot == 26)
            new ReportsGUI(getPlayer());
        return true;
    }
}
