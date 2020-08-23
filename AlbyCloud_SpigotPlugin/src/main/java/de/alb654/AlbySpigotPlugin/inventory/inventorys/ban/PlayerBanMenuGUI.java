package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 12.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.UUID;

public class PlayerBanMenuGUI extends BloodInv {

    public PlayerBanMenuGUI(Player player) {
        super(player, "§bBanMenu", (short) 3);
    }

    @Override
    public void init(Player p) {
        for(int i = 0; i < 9; i++) {
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 7));
            setItem(i + 18, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 7));
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§bBanMenu", 1, "§7Wähle eine Aktion"));
        setItem(10, GetItem.getItem(Material.SKULL_ITEM, "§bGebannte Spieler", 1, "§7Sehe dir alle gebannten Spieler der letzen 30 Tage an", 3));
        setItem(13, GetItem.getItemWithoutAttribs(Material.DIAMOND_SWORD, "§bSpieler bannen", 1, "§7Banne einen Spieler"));
        setItem(16, GetItem.getItemWithoutAttribs(Material.MAP, "§bBangründe", 1, "§7Bearbeite die vorgefertigten Bangründe"));
        setItem(26, GetItem.getItem(Material.NETHER_STAR, "§bLetzte Bans/Unbans", 1, "§7Sehe dir alle gebannten Spieler an"));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {

        switch (slot) {
            case 10:
                new BannedPlayersGUI(getPlayer());
                break;
            case 13:
                SpigotPlugin.getSpigotPlugin().getChatGetTextManager().addGetText(getPlayer(), "§aSchreibe Spielernamen", "§7in den Chat",
                        (player, msg) -> {
                            UUID uuid = NameFetcher.getUUID(msg);
                            if(uuid == null) {
                                player.sendMessage(CloudApi.Bprefix + "§cDieser Spieler existiert nicht!");
                                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                            } else if(SpigotPlugin.getSpigotPlugin().getBanManager().isBanned(uuid.toString())) {
                                player.sendMessage(CloudApi.Bprefix + "§cDieser Spieler ist bereits gebannt/gemutet!");
                                getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                            } else  {
                                new PlayerBanGUI(getPlayer(), msg);
                            }
                        }, player -> {
                            new PlayerBanMenuGUI(getPlayer());
                            getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        });
                break;
            case 16:
                new BanReasonListGUI(getPlayer());
                break;
            case 26:
                new RecentBansUnbansGUI(getPlayer());
                break;
        }

        return true;
    }
}
