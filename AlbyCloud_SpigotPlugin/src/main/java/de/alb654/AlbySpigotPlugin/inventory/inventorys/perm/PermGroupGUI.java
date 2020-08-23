package de.alb654.AlbySpigotPlugin.inventory.inventorys.perm;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 4.7.2020
 */

import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.permissions.PermissionGroup;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class PermGroupGUI extends BloodInv {

    private String group;

    public PermGroupGUI(Player player, String group) {
        super(player, "§ePermissiongruppe §7- §e" + group, (short) 3);
        this.group = group;

        initS();
    }

    private void initS(){
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }
        PermissionGroup pg = new PermissionGroup(group);

        setItem(4, GetItem.getItem(Material.SIGN, "§7Bearbeite die Gruppe", 1));
        setItem(10, GetItem.getItem(Material.SKULL_ITEM, "§eSpieler in der Gruppe", 1, group.equalsIgnoreCase("Default") ? "§7Jeder Spieler ist in dieser Gruppe" : "§7Anzahl: §e" + pg.getPlayerUUIDs().size(),3));
        setItem(13, GetItem.getItem(Material.PISTON_BASE, "§eSpigotpermissions", 1, "§7Anzahl: §e" + pg.getSpigotPerms().size()));
        setItem(15, GetItem.getItem(Material.PISTON_STICKY_BASE, "§eBungeepermissions", 1, "§7Anzahl: §e" + pg.getBungeePerms().size()));
        setItem(26, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public void init(Player p) {}

    @Override
    public boolean onClick(int slot, ClickType type) {

        switch (slot) {
            case 26:
                new PermGroupListGUI(getPlayer());
                break;
            case 10:
                if(!group.equalsIgnoreCase("Default"))
                    new PermGroupPlayerGUI(getPlayer(), group);
                break;
            case 13:
                new PermGroupPermsGUI(getPlayer(), group, true);
                break;
            case 15:
                new PermGroupPermsGUI(getPlayer(), group, false);
                break;
        }

        return true;
    }
}
