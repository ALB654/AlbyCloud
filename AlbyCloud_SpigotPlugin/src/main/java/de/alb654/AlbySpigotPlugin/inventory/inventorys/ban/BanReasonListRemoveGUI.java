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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BanReasonListRemoveGUI extends BloodInv {

    private final HashMap<Integer, BanReason> banReasons;

    public BanReasonListRemoveGUI(Player player) {
        super(player, "§bBangründe §7- §cLöschen", (short) 5);

        banReasons = new HashMap<>();

        init();
    }

    @Override
    public void init(Player p) {}

    public void init() {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
            setItem(i + 36, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 14));
        }

        int currentSlot = 9;
        for(BanReason banReason : SpigotPlugin.getSpigotPlugin().getBanManager().getBanReasons()){
            banReasons.put(currentSlot, banReason);
            currentSlot++;
        }


        for(Map.Entry<Integer, BanReason> entries : banReasons.entrySet()) {
            BanReason br = entries.getValue();
            setItem(entries.getKey(), GetItem.getItemWithoutAttribs(Material.MAP, "§b" + br.getReason(), 1,
                    new ArrayList<>(Arrays.asList("§7Zeit: §b" + Utils.toTimeString(br.getBanTime()),
                            "§7Mute: §b" + (br.isMute() ? "Ja" : "Nein")))));

        }

        setItem(4, GetItem.getItem(Material.SIGN, "§bBangründe §7- §cLöschen", 1, "§7Klicke auf die Bangründe, die du löschen willst"));

        setItem(40, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {

        if(slot == 40)
            new BanReasonListGUI(getPlayer());

        if(slot >= 9 && slot < 36) {
            BanReason br = banReasons.get(slot);
            if(br != null) {
                SpigotPlugin.getSpigotPlugin().getBanManager().removeBanReason(br.getReason());
                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 1f);
                setItem(slot, new ItemStack(Material.AIR));
            }
        }

        return true;
    }
}
