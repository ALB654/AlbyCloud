package de.alb654.AlbySpigotPlugin.inventory.inventorys.report;/*
 * Copyright (c)
 * Created by Aaron Bernert on 27.8.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.inventory.inventorys.ban.PlayerBanGUI;
import de.alb654.AlbySpigotPlugin.report.ReportCase;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import de.alb654.AlbySpigotPlugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class ActiveReportGui extends BloodInv {

    private final ReportCase reportCase;

    public ActiveReportGui(Player player, ReportCase reportCase) {
        super(player, "§2Aktiver Report §8- §2" + reportCase.getName() , (short) 3);

        this.reportCase = reportCase;
        initS();
    }

    @Override
    public void init(Player p) {}

    private void initS() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(10, GetItem.getItem(Material.SKULL_ITEM, "§2" + reportCase.getName(), 1, "§7Diesen Spieler beobachtest du gerade"));
        new BukkitRunnable() {
            @Override
            public void run() {
                setItem(10, GetItem.getHead(reportCase.getName(), "§2" + reportCase.getName(), "§7Diesen Spieler beobachtest du gerade"));
            }
        }.runTask(SpigotPlugin.getSpigotPlugin());

        setItem(14, GetItem.getItem(Material.EMERALD_BLOCK, "§aReport bestätigen", 1, "§7Leitet dich zum Banmenu weiter"));
        setItem(16, GetItem.getItem(Material.REDSTONE_BLOCK, "§cReport widersprechen",1,"§7Widerspricht dem Report"));

        setItem(22, GetItem.getItem(Material.WATCH, "§7Restzeit: §2" + Utils.toTimeString(reportCase.getStartedCheckingTime() + TimeUnit.MINUTES.toMillis(15) - System.currentTimeMillis()), 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {

        switch (slot) {
            case 14:
                new PlayerBanGUI(getPlayer(), reportCase.getName());
                break;
            case 16:
                SpigotPlugin.getSpigotPlugin().getReportManager().disagreeReport(NameFetcher.getName(getPlayer().getUniqueId()), reportCase);
                getPlayer().closeInventory();
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                break;
        }

        return true;
    }
}
