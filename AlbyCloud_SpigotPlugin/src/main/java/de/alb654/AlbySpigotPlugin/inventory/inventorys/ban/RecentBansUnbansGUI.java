package de.alb654.AlbySpigotPlugin.inventory.inventorys.ban;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 13.7.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.ban.BanPlayer;
import de.alb654.AlbySpigotPlugin.recent.RecentAction;
import de.alb654.AlbySpigotPlugin.recent.RecentBanAction;
import de.alb654.AlbySpigotPlugin.recent.RecentUnbanAction;
import de.alb654.AlbySpigotPlugin.inventory.BloodInv;
import de.alb654.AlbySpigotPlugin.utils.GetItem;
import de.alb654.AlbySpigotPlugin.utils.NameFetcher;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class RecentBansUnbansGUI extends BloodInv {

    private final ArrayList<RecentAction> banActions;
    private final ArrayList<RecentAction> unbanActions;

    public RecentBansUnbansGUI(Player player) {
        super(player, "§bLetzte Bans/Unbans", (short) 4);

        banActions = SpigotPlugin.getSpigotPlugin().getRecentActionsManager().getRecentActions(RecentAction.ActionType.BAN);
        unbanActions = SpigotPlugin.getSpigotPlugin().getRecentActionsManager().getRecentActions(RecentAction.ActionType.UNBAN);

        init();
    }

    @Override
    public void init(Player p) {}

    private void init() {
        for(int i = 0; i < 9; i++){
            setItem(i, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));
            setItem(i + 27, GetItem.getItem(Material.STAINED_GLASS_PANE, " ", 1, 7));

            if(banActions.size() > i) {
                RecentBanAction banAction = (RecentBanAction) banActions.get(i);
                BanPlayer bp = SpigotPlugin.getSpigotPlugin().getBanManager().getBannedPlayer(banAction.getSuspectUUID());

                if(bp == null) {
                    setItem(i + 9, GetItem.getItemWithoutAttribs(Material.DIAMOND_SWORD, "§c" + NameFetcher.getName(UUID.fromString(banAction.getSuspectUUID())) + " §bgebannt", 1,
                            "§cNicht mehr gebannt!"));
                } else
                    setItem(i + 9, GetItem.getItemWithoutAttribs(Material.DIAMOND_SWORD, "§c" + bp.getName() + " §bgebannt", 1,
                        new ArrayList<>(Arrays.asList("§7von: §b" + bp.getByName(),
                                                      "§7Grund: §b" + bp.getReason(),
                                                      "§7Mute: §b" + (bp.isMute() ? "Ja" : "Nein"),
                                                      "§7bis: §b" + (bp.getUntilTime() == -1 ? "PERMANENT" : new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(bp.getUntilTime()))),
                                                      "§7am: §b" + new SimpleDateFormat("dd.MM.yyyy").format(new Date(banAction.getActionTime())),
                                                      "§7um: §b" + new SimpleDateFormat("HH:mm:ss").format(new Date(banAction.getActionTime()))))));
            }

            if(unbanActions.size() > i) {
                RecentUnbanAction unbanAction = (RecentUnbanAction) unbanActions.get(i);
                setItem(i + 18, GetItem.getItem(Material.FEATHER, "§c" + NameFetcher.getName(UUID.fromString(unbanAction.getSuspectUUID())) + "§b geunbannt", 1,
                        new ArrayList<>(Arrays.asList("§7von: §b" + unbanAction.getUnbanBy(),
                                                      "§7am: §b" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(unbanAction.getActionTime())),
                                                      "§7Grund: §b" + unbanAction.getBanReason(),
                                                      "§7gebannt von: §b" + unbanAction.getNormalBanBy(),
                                                      "§7ürsprünglich bis: §b" + (unbanAction.getNormalBanUntil() == -1 ? "PERMANENT" : new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(unbanAction.getNormalBanUntil())))))));
            }
        }

        setItem(4, GetItem.getItem(Material.SIGN, "§bLetzte Bans/Unbans", 1, "§7Hier siehst du zuletzt gebannte/geunbannte Spieler"));
        setItem(35, GetItem.getItem(Material.DARK_OAK_DOOR_ITEM, "§cZurück", 1));
    }

    @Override
    public boolean onClick(int slot, ClickType type) {
        if(slot == 35) {
            new PlayerBanMenuGUI(getPlayer());
        }
        return true;
    }
}
