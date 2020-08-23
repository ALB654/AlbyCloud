package de.alb654.AlbySpigotPlugin.inventory.inventorys.report;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.8.2020
 */

import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ReportListInv extends BloodInv {

    public ReportListInv(Player player) {
        super(player, "§2Reports", (short) 6);
    }

    @Override
    public void init(Player p) {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 45, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§2Reports", 1, "§7Hier werden alle Reports angezeigt"));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        return false;
    }
}
