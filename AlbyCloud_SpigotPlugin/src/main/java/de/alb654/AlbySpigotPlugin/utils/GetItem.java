package de.alb654.AlbySpigotPlugin.utils;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 3.7.2020
 */

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GetItem {

    public static ItemStack getHead(String playername, String itemname, List<String> lore){
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        Class<?> skullMetaClass;
        try {
            skullMetaClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftMetaSkull");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        final ItemStack head = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
        final SkullMeta headm = (SkullMeta) head.getItemMeta();
        headm.setDisplayName(itemname);
        headm.setLore(lore);
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headm, GameProfileBuilder.fetch(NameFetcher.getUUID(playername)));
        } catch (Exception ex) {}
        headm.setLore(lore);
        head.setItemMeta(headm);
        return head;
    }

    public static ItemStack getHead(String playername, String itemname, String lore){
        return getHead(playername, itemname, Collections.singletonList(lore));
    }

    public static ItemStack getHead(String playername, String itemname){
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        Class<?> skullMetaClass;
        try {
            skullMetaClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftMetaSkull");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        final ItemStack head = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
        final SkullMeta headm = (SkullMeta) head.getItemMeta();
        headm.setDisplayName(itemname);
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headm, GameProfileBuilder.fetch(NameFetcher.getUUID(playername)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        head.setItemMeta(headm);
        return head;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, ArrayList<String> lore)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        m.setLore(lore);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItemWithoutAttribs(Material material, String name, int anzahl, ArrayList<String> lore)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        m.setLore(lore);
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, String lore)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        List<String> l = new ArrayList<String>();
        l.add(lore);
        m.setLore(l);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, String... lore)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        m.setLore(Arrays.asList(lore));
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItemWithoutAttribs(Material material, String name, int anzahl, String lore)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        List<String> l = new ArrayList<String>();
        l.add(lore);
        m.setLore(l);
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItemWithoutAttribs(Material material, String name, int anzahl, String... lore)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        m.setLore(Arrays.asList(lore));
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, String lore, int subid)
    {
        ItemStack item = new ItemStack(material, anzahl, (short) subid);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        List<String> l = new ArrayList<>();
        l.add(lore);
        m.setLore(l);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, List<String> lore, int subid)
    {
        ItemStack item = new ItemStack(material, anzahl, (short) subid);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        m.setLore(lore);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItemWithNoEnchantmentShown(Material material, String name, int anzahl, ArrayList<String> lore, Enchantment enchantment, int lvl)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        m.setLore(lore);
        m.addEnchant(enchantment, lvl, true);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, ArrayList<String> lore, Enchantment enchantment, int lvl)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        m.setLore(lore);
        m.addEnchant(enchantment, lvl, true);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, String lore, Enchantment enchantment, int lvl)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        List<String> l = new ArrayList<String>();
        l.add(lore);
        m.setLore(l);
        m.addEnchant(enchantment, lvl, true);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItemWithNoEnchantmentShown(Material material, String name, int anzahl, String lore, Enchantment enchantment, int lvl)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        List<String> l = new ArrayList<String>();
        l.add(lore);
        m.setLore(l);
        m.addEnchant(enchantment, lvl, true);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, String lore, int subid, Enchantment enchantment, int lvl)
    {
        ItemStack item = new ItemStack(material, anzahl, (short) subid);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        List<String> l = new ArrayList<String>();
        l.add(lore);
        m.setLore(l);
        m.addEnchant(enchantment, lvl, true);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItemWithNoEnchantmentShown(Material material, String name, int anzahl, String lore, int subid, Enchantment enchantment, int lvl)
    {
        ItemStack item = new ItemStack(material, anzahl, (short) subid);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        List<String> l = new ArrayList<String>();
        l.add(lore);
        m.setLore(l);
        m.addEnchant(enchantment, lvl, true);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, String lore, Enchantment enchantment, int lvl, boolean unbreakable)
    {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        if(unbreakable)
            m.setUnbreakable(true);
        List<String> l = new ArrayList<String>();
        l.add(lore);
        m.setLore(l);
        m.addEnchant(enchantment, lvl, true);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getFarbigeRüstung(Material mat, String Name, String Lore, Color color) {
        ItemStack i = new ItemStack(mat);
        LeatherArmorMeta lm = (LeatherArmorMeta)i.getItemMeta();
        lm.setColor(color);
        lm.setDisplayName(Name);
        List<String> l = new ArrayList<>();
        l.add(Lore);
        lm.setLore(l);
        i.setItemMeta(lm);
        return i;
    }

    public static ItemStack getItem(Material material, String name, int anzahl) {
        ItemStack item = new ItemStack(material, anzahl);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack getItem(Material material, String name, int anzahl, int subid) {
        ItemStack item = new ItemStack(material, anzahl, (short)subid);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        item.setItemMeta(m);
        return item;
    }
}
