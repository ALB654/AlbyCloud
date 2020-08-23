package de.alb654.AlbySpigotPlugin.inventory.inventorys.perm;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 4.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.BloodPlayer;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.permissions.PermissionGroup;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PermGroupPlayerGUI extends BloodInv {

    private final String group;

    boolean notFull = true;

    private int nextSlot = 9;

    public PermGroupPlayerGUI(Player player, String group) {
        super(player, "§eSpieler §7- §e" + group, (short) 5);

        this.group = group;

        initS();
    }

    public void initS() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 36, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        for(String playerUUID : new PermissionGroup(group).getPlayerUUIDs()) {
            if(nextSlot < 36) {
                String player = NameFetcher.getName(UUID.fromString(playerUUID));

                setItem(nextSlot, GetItem.getItem(Material.SKULL_ITEM, "§e" + player, 1));
                new BukkitRunnable() {
                    int s = nextSlot;
                    @Override
                    public void run() {
                        setItem(s, GetItem.getHead(player, "§e"+ player));
                    }
                }.runTask(SpigotPlugin.getSpigotPlugin());

                if(nextSlot == 35)
                    notFull = false;
                else
                    nextSlot++;
            }
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§7Hier werden alle Spieler angezeigt.", 1));

        if(notFull) {
            setItem(39, GetItem.getItem(Material.FLOWER_POT_ITEM, "§aSpieler hinzufügen", 1));
            setItem(41, GetItem.getItem(Material.REDSTONE_BLOCK, "§cSpieler entfernen", 1));
        } else
            setItem(40, GetItem.getItem(Material.REDSTONE_BLOCK, "§cSpieler entfernen", 1));

        setItem(44, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§eZurück", 1));
    }

    @Override
    public void init(Player p) {}

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(slot == 44) {
            new PermGroupGUI(getPlayer(), group);
        }

        if(notFull) {
            if(slot == 39) {
                SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Spielername", "§7in den Chat", (player, msg) -> {

                    BloodPlayer bp = new BloodPlayer(msg);

                    if(bp.exists()) {
                        if(bp.getPermissionGroups().contains(group)) {
                            player.sendMessage(CloudApi.Bprefix + "§cDieser Spieler ist bereits in der Gruppe!");
                            getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        } else {
                            bp.addPermissionGroup(group);
                            new PermGroupPlayerGUI(getPlayer(), group);
                            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                        }
                    } else {
                        player.sendMessage(CloudApi.Bprefix + "§cDieser Spieler war noch nie online!");
                        getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                    }

                }, (player -> {
                    new PermGroupPlayerGUI(getPlayer(), group);
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                }));
            }
            if(slot == 41) {
                new PermGroupPlayerRemoveGUI(getPlayer(), group);
            }
        } else {
            if(slot == 40) {
                new PermGroupPlayerRemoveGUI(getPlayer(), group);
            }
        }

        return true;
    }
}
