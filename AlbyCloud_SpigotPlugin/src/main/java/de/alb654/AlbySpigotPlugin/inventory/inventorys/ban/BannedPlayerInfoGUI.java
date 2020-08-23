package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 15.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.ban.BanPlayer;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BannedPlayerInfoGUI extends BloodInv<BanPlayer> {

    public BannedPlayerInfoGUI(Player player, BanPlayer banPlayer) {
        super(player, "§b" + banPlayer.getName() + " §7- §bInfo", (short) 3, banPlayer);
    }

    @Override
    public void init(Player p) {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        BanPlayer bp = getValue();
        setItem(4, GetItem.getHead(getValue().getName(), "§b" + getValue().getName(), "§7Baninfo für " + getValue().getName()));
        setItem(11, GetItem.getItemWithoutAttribs(Material.MAP, "§bInfo§7: §c" + getValue().getName(), 1, new ArrayList<>(Arrays.asList(
                "§7von: §b" + bp.getByName(),
                "§7Grund: §b" + bp.getReason(),
                "§7Mute: §b" + (bp.isMute() ? "Ja" : "Nein"),
                "§7bis: §b" + (bp.getUntilTime() == -1 ? "PERMANENT" : new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(bp.getUntilTime()))),
                "§7am: §b" + new SimpleDateFormat("dd.MM.yyyy").format(new Date(bp.getBanTime())),
                "§7um: §b" + new SimpleDateFormat("HH:mm:ss").format(new Date(bp.getBanTime()))
        ))));
        setItem(15, GetItem.getItem(Material.FEATHER, "§bUnbannen", 1, "§7Unbanne den Spieler"));
        setItem(26, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        switch (slot) {
            case 15:
                SpigotPlugin.getSpigotPlugin().getBanManager().unbanPlayer(getValue().getUuid(), getPlayer().getName());
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                new BannedPlayersGUI(getPlayer());
                break;
            case 26:
                new BannedPlayersGUI(getPlayer());
                break;
        }
        return true;
    }
    
}
