package de.alb654.AlbySpigotPlugin.inventory.inventorys.perm;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.permissions.PermissionGroup;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class PermGroupListRemoveGUI extends BloodInv<HashMap<Integer, String>> {

    public PermGroupListRemoveGUI(Player player) {
        super(player, "§ePermissiongruppen §7- §cLöschen", (short) 4, new HashMap<>());
    }

    @Override
    public void init(Player p) {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
            setItem(i + 27, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
        }

        int nextSlot = 9;

        for(String group : PermissionGroup.getAllGroupNames()) {
            if(nextSlot < 27) {
                PermissionGroup pg = new PermissionGroup(group);
                ArrayList<String> lore = new ArrayList<>();
                lore.add("§7Spigot-Perms: §e" + pg.getSpigotPerms().size());
                lore.add("§7Bungee-Perms: §e" + pg.getBungeePerms().size());
                setItem(nextSlot, GetItem.getItem(Material.CHEST, group.equalsIgnoreCase("Default") ? "§e"+ group + "§7(§cNICHT LÖSCHBAR§7)" : "§e" + group, 1, lore));
                getValue().put(nextSlot, group);
                nextSlot++;
            }
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§7Klicke auf die Gruppen die du löschen willst.", 1));
        setItem(31, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(slot == 31) {
            new PermGroupListGUI(getPlayer());
        }
        if(slot >= 9 && slot < 27) {
            String group = getValue().get(slot);
            if(group != null && !group.equalsIgnoreCase("Default")) {
                PermissionGroup.removeGroup(group);
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                setItem(slot, new ItemStack(Material.AIR));
                getValue().remove(slot);
            }
        }
        return true;
    }
}
