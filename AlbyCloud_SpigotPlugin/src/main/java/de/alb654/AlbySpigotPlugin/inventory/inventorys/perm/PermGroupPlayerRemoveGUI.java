package de.alb654.AlbySpigotPlugin.inventory.inventorys.perm;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 4.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.BloodPlayer;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.permissions.PermissionGroup;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PermGroupPlayerRemoveGUI extends BloodInv {

    private final HashMap<Integer, String> players = new HashMap<>();
    private final String group;

    private int nextSlot = 9;

    public PermGroupPlayerRemoveGUI(Player player, String group) {
        super(player, "§eSpieler §7 - §e" + group + "§7- §cLöschen", (short)5);
        this.group = group;

        initS();
    }

    private void initS() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
            setItem(i + 36, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
        }

        for(String playerUUID : new PermissionGroup(group).getPlayerUUIDs()) {
            if(nextSlot < 36) {
                String player = NameFetcher.getName(UUID.fromString(playerUUID));

                setItem(nextSlot, GetItem.getItem(Material.SKULL_ITEM, "§e" + player, 1, 3));
                new BukkitRunnable() {
                    int s = nextSlot;
                    @Override
                    public void run() {
                        setItem(s, GetItem.getHead(player, "§e"+ player));
                    }
                }.runTask(SpigotPlugin.getSpigotPlugin());

                players.put(nextSlot, player);

                nextSlot++;
            }
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§7Klicke auf Spieler, um sie zu löschen.", 1));

        setItem(40, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§eZurück", 1));
    }

    @Override
    public void init(Player p) {}

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(slot == 40) {
            new PermGroupPlayerGUI(getPlayer(), group);
        }

        if(slot >= 9 && slot < 36) {
            String player = players.get(slot);
            if(player != null) {
                new BloodPlayer(player).removePermissionGroup(group);
                setItem(slot, new ItemStack(Material.AIR));
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                players.remove(slot);
            }
        }

        return true;
    }
}
