package de.alb654.AlbySpigotPlugin.inventory.inventorys.perm;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.permissions.PermissionGroup;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;

public class PermGroupListGUI extends BloodInv {

    private HashMap<Integer, String> groups = new HashMap<>();
    private boolean notFull;

    public PermGroupListGUI(Player player) {
        super(player, "§ePermissiongruppen", (short) 4);

        notFull = true;

        init();
    }


    public void init() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 27, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        int nextSlot = 9;

        for(String group : PermissionGroup.getAllGroupNames()) {
            if(nextSlot < 27) {
                PermissionGroup pg = new PermissionGroup(group);
                ArrayList<String> lore = new ArrayList<>();
                lore.add("§7Spigot-Perms: §e" + pg.getSpigotPerms().size());
                lore.add("§7Bungee-Perms: §e" + pg.getBungeePerms().size());
                setItem(nextSlot, GetItem.getItem(Material.CHEST, "§e"+ group, 1, lore));
                groups.put(nextSlot, group);

                if(nextSlot == 26)
                    notFull = false;
                else
                    nextSlot++;
            }
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§7Hier werden alle Permissiongruppen angezeigt.", 1));

        if(notFull) {
            setItem(30, GetItem.getItem(Material.FLOWER_POT_ITEM, "§aGruppe hinzufügen", 1));
            setItem(32, GetItem.getItem(Material.REDSTONE_BLOCK, "§cGruppen löschen", 1));
        } else
            setItem(31, GetItem.getItem(Material.REDSTONE_BLOCK, "§cGruppen löschen", 1));
    }

    @Override
    public void init(Player p) {}

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(notFull && slot == 30) {
            SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Gruppennamen", "§7in den Chat", (player, msg) -> {
                PermissionGroup.addGroup(msg);
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                new PermGroupListGUI(player);
            }, player -> {
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                new PermGroupListGUI(player);
            });
        }

        if(notFull) {
            if (slot == 32)
                new PermGroupListRemoveGUI(getPlayer());
        } else
            if(slot == 31)
                new PermGroupListRemoveGUI(getPlayer());

        if(slot >= 9 && slot < 27) {
            String group = groups.get(slot);
            if(group != null)
                new PermGroupGUI(getPlayer(), group);
        }

        return true;
    }
}
