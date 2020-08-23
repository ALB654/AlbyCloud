package de.alb654.AlbySpigotPlugin.events;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        boolean b = SpigotPlugin.getSpigotPlugin().getBloodInvManager().doClick(p, e.getRawSlot(), e.getClick());
        if(b) e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        SpigotPlugin.getSpigotPlugin().getBloodInvManager().removeLobbyInv((Player) e.getPlayer());
    }

}
