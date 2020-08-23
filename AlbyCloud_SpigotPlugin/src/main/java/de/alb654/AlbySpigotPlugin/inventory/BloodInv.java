package de.alb654.AlbySpigotPlugin.inventory;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class BloodInv<V> {

    private final Inventory inventory;
    private final Player player;
    private V value;

    public BloodInv(Player player, String name, short zeilen) {
        this.inventory = Bukkit.createInventory(player, zeilen*9, name);
        this.player = player;

        SpigotPlugin.getSpigotPlugin().getBloodInvManager().addInv(this);

        init(player);
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
    }

    public BloodInv(Player player, String name, short zeilen, V value) {
        this.inventory = Bukkit.createInventory(player, zeilen*9, name);
        this.player = player;
        this.value = value;

        SpigotPlugin.getSpigotPlugin().getBloodInvManager().addInv(this);

        init(player);
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
    }

    public BloodInv(Player player, String name, InventoryType type, V v) {
        this.inventory = Bukkit.createInventory(null,  type, name);
        this.player = player;
        this.value = v;

        SpigotPlugin.getSpigotPlugin().getBloodInvManager().addInv(this);

        init(player);
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
    }

    public BloodInv(Player player, String name, InventoryType type) {
        this.inventory = Bukkit.createInventory(null,  type, name);
        this.player = player;

        SpigotPlugin.getSpigotPlugin().getBloodInvManager().addInv(this);

        init(player);
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
    }

    public abstract void init(Player p);

    public abstract boolean onClick(int slot, ClickType type);

    public Player getPlayer() {
        return player;
    }

    public Inventory getInv() {
        return inventory;
    }

    public void setItem(int slot, ItemStack item) {
        this.inventory.setItem(slot, item);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
