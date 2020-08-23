package de.alb654.AlbySpigotPlugin.inventory.inventorys.report;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.8.2020
 */

import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ReportListInv extends BloodInv {

    public ReportListInv(Player player) {
        super(player, "§2Reports", (short) 6);
    }

    @Override
    public void init(Player p) {

    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        return false;
    }
}
