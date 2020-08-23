package de.alb654.AlbySpigotPlugin.inventory;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;

public class BloodInvManager {

    private ArrayList<BloodInv<?>> invs = new ArrayList<>();

    public void addInv(BloodInv<?> bloodInventory) {
        if(!invs.contains(bloodInventory))
            invs.add(bloodInventory);
    }

    public BloodInv<?> getInv(Player p) {
        for(BloodInv<?> li : invs) {
            if(li.getPlayer().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                return li;
            }
        }
        return null;
    }

    public boolean doClick(Player p, int slot, ClickType type) {
        BloodInv<?> l = getInv(p);
        if(l != null)
            return l.onClick(slot, type);
        return false;
    }

    public void removeLobbyInv(Player p) {
        invs.remove(getInv(p));
    }

}
