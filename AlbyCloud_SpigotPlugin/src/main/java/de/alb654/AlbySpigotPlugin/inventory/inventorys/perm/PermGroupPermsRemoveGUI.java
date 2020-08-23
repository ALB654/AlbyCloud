package de.alb654.AlbySpigotPlugin.inventory.inventorys.perm;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 5.7.2020
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
import java.util.List;

public class PermGroupPermsRemoveGUI extends BloodInv {

    private final String group;
    private final boolean spigot;
    private final HashMap<Integer, ArrayList<String>> sites;

    private int currentSite;

    public PermGroupPermsRemoveGUI(Player player, String group, boolean spigot) {
        super(player, (spigot ? "§eSpigotpermissions" : "§eBungeepermissions") + " §7- §cLöschen", (short) 5);

        this.group = group;
        this.spigot = spigot;
        this.sites = new HashMap<>();
        this.currentSite = 0;

        fillSites();
        initS();
    }

    private void fillSites() {
        PermissionGroup pg = new PermissionGroup(group);
        ArrayList<String> perms = spigot ? pg.getSpigotPerms() : pg.getBungeePerms();
        for(int site = 0; site <= perms.size() / 27; site++) {
            sites.put(site, new ArrayList<>(perms.subList(site * 27, Math.min((site * 27) + 27, perms.size()))));
        }

    }

    private void initS() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
            setItem(i + 36, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
        }

        setItem(3, GetItem.getItem(Material.SIGN, "§7Permission zum löschen anklicken", 1));
        setItem(5, spigot ? GetItem.getItem(Material.PISTON_BASE, "§eSpigotpermissions", 1, "§7Klicke zum wechseln")
                : GetItem.getItem(Material.PISTON_STICKY_BASE, "§eBungeepermissions", 1, "§7Klicke zum wechseln"));

        setItem(40, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§eZurück", 1));

        if(sites.size() > 1) {
            setItem(38, GetItem.getItem(Material.ARROW, "§eSeite zurück", 1));
            setItem(42, GetItem.getItem(Material.ARROW, "§eSeite vor", 1));
        }

        showCurrentSite();
    }

    private void showCurrentSite() {
        List<String> perms = sites.get(currentSite);
        if(perms != null && !perms.isEmpty()){
            for(int i = 0; i < 27; i++)
                setItem(i + 9, new ItemStack(Material.AIR));

            for(int i = 0; i < perms.size(); i++) {
                setItem(i + 9, GetItem.getItem(Material.NAME_TAG, "§e" + perms.get(i), 1));
            }
        }
    }

    @Override
    public void init(Player p) {}

    @Override
    public boolean onClick(int slot, ClickType type) {

        if(slot == 40) {
            new PermGroupPermsGUI(getPlayer(), group, spigot);
        } else if(slot == 5) {
            new PermGroupPermsGUI(getPlayer(), group, !spigot);
        } else if(slot >= 9 && slot < 36) {
            if(getInv().getItem(slot) == null || getInv().getItem(slot).getType() == Material.AIR)
                return true;

            String perm = sites.get(currentSite).get(slot - 9);
            if(perm != null){
                PermissionGroup pg = new PermissionGroup(group);
                if(spigot) {
                    pg.removeSpigotPerm(perm);
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                } else {
                    pg.removeBungeePerm(perm);
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                }
                setItem(slot, new ItemStack(Material.AIR));
                sites.get(currentSite).set(slot - 9, null);
            }
        }

        if(sites.size() > 1) {
            if(slot == 38) {
                currentSite--;
                if(currentSite < 0)
                    currentSite = sites.size() - 1;

                showCurrentSite();
            } else if(slot == 42) {
                currentSite++;
                if(currentSite >= sites.size())
                    currentSite = 0;

                showCurrentSite();
            }
        }

        return true;
    }
}
