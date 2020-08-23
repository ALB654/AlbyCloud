package de.alb654.AlbySpigotPlugin.inventory.inventorys.perm;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 5.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
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

public class PermGroupPermsGUI extends BloodInv {

    private static final ArrayList<String> spigotSystemPermsLore = new ArrayList<>();
    private static final ArrayList<String> bungeeSystemPermsLore = new ArrayList<>();

    static {
        spigotSystemPermsLore.add(" ");
        spigotSystemPermsLore.add("§7» §eBloodMC.OP");
        spigotSystemPermsLore.add("§7» §eBloodMC.Permissions");
        spigotSystemPermsLore.add("§7» §eBloodMC.Ban");
        spigotSystemPermsLore.add("§7» §eBloodMC.placeSigns");
        spigotSystemPermsLore.add("§7» §eBloodMC.Jump");
        spigotSystemPermsLore.add("§7» §eBloodMC.Blacklist");
        spigotSystemPermsLore.add("§7» §eBloodMC.Nick");

        bungeeSystemPermsLore.add(" ");
        bungeeSystemPermsLore.add("§7» §eBloodMC.CloudUpdates");
        bungeeSystemPermsLore.add("§7» §eBloodMC.BanMsg");
        bungeeSystemPermsLore.add("§7» §eBloodMC.TeamChat");
    }

    private final String group;
    private final boolean spigot;
    private final HashMap<Integer, ArrayList<String>> sites;

    private int currentSite;

    public PermGroupPermsGUI(Player player, String group, boolean spigot) {
        super(player, (spigot ? "§eSpigotpermissions" : "§eBungeepermissions") + " §7- §e" + group, (short) 5);

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
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 36, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        setItem(3, GetItem.getItem(Material.SIGN, "§7Hier werden alles Permissions angezeigt", 1));
        setItem(5, spigot ? GetItem.getItem(Material.PISTON_BASE, "§eSpigotpermissions", 1, "§7Klicke zum wechseln")
                              : GetItem.getItem(Material.PISTON_STICKY_BASE, "§eBungeepermissions", 1, "§7Klicke zum wechseln"));

        setItem(8, spigot ? GetItem.getItem(Material.FEATHER, "§eSystem-Permissions", 1, spigotSystemPermsLore)
                              : GetItem.getItem(Material.FEATHER, "§eSystem-Permissions", 1, bungeeSystemPermsLore));

        setItem(39, GetItem.getItem(Material.FLOWER_POT_ITEM, "§aPermission hinzufügen", 1));
        setItem(41, GetItem.getItem(Material.REDSTONE_BLOCK, "§cPermissions löschen", 1));
        setItem(44, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§eZurück", 1));

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

        if(slot == 44) {

            new PermGroupGUI(getPlayer(), group);

        } else if(slot == 39) {

            SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Permission", "§7in den Chat", (player, msg) -> {
                PermissionGroup pg = new PermissionGroup(group);

                if (spigot) {

                    if(!pg.getBungeePerms().contains(msg)) {
                        pg.addSpigotPerm(msg);
                        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                        new PermGroupPermsGUI(getPlayer(), group, spigot);
                    } else {
                        getPlayer().sendMessage(CloudApi.Bprefix + "§cDiese Gruppe besitzt bereits diese Permission!");
                        getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                    }

                } else {

                    if(!pg.getBungeePerms().contains(msg)) {
                        new PermissionGroup(group).addBungeePerm(msg);
                        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                        new PermGroupPermsGUI(getPlayer(), group, spigot);
                    } else {
                        getPlayer().sendMessage(CloudApi.Bprefix + "§cDiese Gruppe besitzt bereits diese Permission!");
                        getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                    }
                }

            }, (player) -> {
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                new PermGroupPermsGUI(getPlayer(), group, spigot);
            });

        } else if(slot == 41) {
            new PermGroupPermsRemoveGUI(getPlayer(), group, spigot);
        } else if(slot == 5) {
            new PermGroupPermsGUI(getPlayer(), group, !spigot);
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
