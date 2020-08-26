package de.alb654.AlbySpigotPlugin.inventory.inventorys.report;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.8.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.report.ReportCase;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportsGUI extends BloodInv {

    private final HashMap<Integer, ArrayList<ReportCase>> sites;

    private int currentSite;

    public ReportsGUI(Player player) {
        super(player, "§2Reports", (short) 6);

        this.sites = new HashMap<>();
        this.currentSite = 0;

        fillSites();
        initS();
    }

    private void fillSites() {
        ArrayList<ReportCase> reports = SpigotPlugin.getSpigotPlugin().getReportManager().getReports();
        for(int site = 0; site <= reports.size() / 27; site++) {
            sites.put(site, new ArrayList<>(reports.subList(site * 27, Math.min((site * 27) + 27, reports.size()))));
        }
    }

    private void initS() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 45, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§2Reports", 1, "§7Hier werden alle Reports angezeigt"));
        setItem(52, GetItem.getItem(Material.NETHER_STAR, "§2Letzte Reports", 1, "§7Alle zuletzt bearbeiteten Reports"));

        if(sites.size() > 1) {
            setItem(48, GetItem.getItem(Material.ARROW, "§eSeite zurück", 1));
            setItem(50, GetItem.getItem(Material.ARROW, "§eSeite vor", 1));
        }

        showCurrentSite();
    }

    private void showCurrentSite() {
        List<ReportCase> reports = sites.get(currentSite);
        if(reports != null && !reports.isEmpty()){
            for(int i = 0; i < 27; i++)
                setItem(i + 9, new ItemStack(Material.AIR));

            for(int i = 0; i < reports.size(); i++) {
                ReportCase rc = reports.get(i);
                if(rc.isActive()) {
                    setItem(i + 9, GetItem.getItemWithoutAttribs(Material.IRON_SWORD, "§2" + reports.get(i).getName(), 1,
                            "§7wird bearbeitet von: §2" + NameFetcher.getName(UUID.fromString(rc.getCheckedByUUID())), "§7Gestartet um: §2" +
                            new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(rc.getStartedCheckingTime()))));
                } else
                    setItem(i + 9, GetItem.getItem(Material.NAME_TAG, "§2" + reports.get(i).getName(), 1, "§7anzahl: §2" + rc.getReporterAndReasons().size()));
            }
        }
    }

    @Override
    public void init(Player p) {}

    @Override
    public boolean onClick(int slot, ClickType type) {

        if(slot == 52) {
            new LatestClosedReportsGUI(getPlayer());
        } else if(slot >= 9 && slot < 36) {
            if(getInv().getItem(slot) == null || getInv().getItem(slot).getType() == Material.AIR)
                return true;

            ReportCase rc = sites.get(currentSite).get(slot - 9);
            if(rc != null){
                new ReportGUI(getPlayer(), rc);
            }

        } else if(sites.size() > 1) {

            if(slot == 48) {
                currentSite--;
                if(currentSite < 0)
                    currentSite = sites.size() - 1;

                showCurrentSite();
            } else if(slot == 50) {
                currentSite++;
                if(currentSite >= sites.size())
                    currentSite = 0;

                showCurrentSite();
            }
        }

        return true;
    }
}
