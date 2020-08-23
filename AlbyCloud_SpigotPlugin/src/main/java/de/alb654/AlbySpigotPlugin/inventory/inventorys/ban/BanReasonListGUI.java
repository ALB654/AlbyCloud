package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.ban.BanReason;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;

public class BanReasonListGUI extends BloodInv {

    private final ArrayList<BanReason> banReasons;

    public BanReasonListGUI(Player player) {
        super(player, "§bBangründe", (short) 5);

        banReasons = SpigotPlugin.getSpigotPlugin().getBanManager().getBanReasons();

        init();
    }

    @Override
    public void init(Player p) {}

    public void init() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 36, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
        }

        int currentSlot = 9;

        for(BanReason br : banReasons) {
            if(currentSlot < 36) {
                setItem(currentSlot, GetItem.getItemWithoutAttribs(Material.MAP, "§b" + br.getReason(), 1,
                        new ArrayList<>(Arrays.asList("§7Zeit: §b" + Utils.toTimeString(br.getBanTime()),
                                                      "§7Mute: §b" + (br.isMute() ? "Ja" : "Nein")))));
                currentSlot++;
            }
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§bBangründe", 1, "§7Hier werden alle vorgefertigten Bangründe angezeigt"));
        setItem(38, GetItem.getItem(Material.FLOWER_POT_ITEM, "§aGrund hinzufügen", 1, "§7Füge einen Grund hinzu"));
        setItem(42, GetItem.getItem(Material.REDSTONE_BLOCK, "§aGründe löschen", 1, "§7Lösche Bangründe"));
        setItem(44, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {

        switch (slot) {
            case 38:
                new BanReasonCreateGUI(getPlayer());
                break;
            case 42:
                new BanReasonListRemoveGUI(getPlayer());
                break;
            case 44:
                new PlayerBanMenuGUI(getPlayer());
                break;
        }

        return true;
    }
}
