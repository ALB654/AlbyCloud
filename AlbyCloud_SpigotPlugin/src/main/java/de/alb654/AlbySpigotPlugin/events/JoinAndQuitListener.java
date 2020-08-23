package de.alb654.AlbySpigotPlugin.events;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 28.6.2020
 */

import de.alb654.AlbySpigotPlugin.SpigotPlugin;
import de.alb654.AlbySpigotPlugin.api.BloodPlayer;
import de.alb654.AlbySpigotPlugin.api.CloudApi;
import de.alb654.AlbySpigotPlugin.api.server.CloudServer;
import de.alb654.AlbySpigotPlugin.nick.NickManager;
import de.alb654.AlbySpigotPlugin.permissions.PermissionGroup;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class JoinAndQuitListener implements Listener {

    public static String tablistColor = "§b";

    {
        if(!PermissionGroup.existsGroup("Default"))
            PermissionGroup.addGroup("Default");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        CloudServer thisServer = CloudApi.getInstance().getServerManager().getThisServer();
        thisServer.addOnePlayer();
        thisServer.updateStatsToMaster();

        final Player player = e.getPlayer();
        setPermissions(player);
        setTablistTitle(player, thisServer);

        NickManager nickManager = SpigotPlugin.getSpigotPlugin().getNickManager();
        if(nickManager.isNickedInDB(player.getUniqueId().toString()) && !CloudApi.getInstance().getServerManager().getThisServer().getId().startsWith("LOBBY")) {
            nickManager.realizeNick(player);
            e.setJoinMessage("§e" + player.getName() + " joined the game");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CloudServer thisServer = CloudApi.getInstance().getServerManager().getThisServer();
        thisServer.removeOnePlayer();
        thisServer.updateStatsToMaster();
    }

    private void setTablistTitle(Player player, CloudServer server) {
        String header  = "\n§7● " + tablistColor + "§lBloodMC.eu §7●\n"
                       + "§7Dein " + tablistColor + "Minecraft§7-" + tablistColor + "Netzwerk§7!\n";
        String footer = "\n§7Du siehst einen Hacker?  /report <" + tablistColor + "Name§7> \n\n"
                      + "§7  Du befindest dich auf • " + tablistColor + " " + server.getId() + "  \n"
                      + "§7Discord • " + tablistColor + "dc.BloodMC.eu\n"
                      + "§7Forum • " + tablistColor + "forum.BloodMC.eu\n";

        sendTablist(player, header, footer);
    }

    private void sendTablist(Player p, String header, String footer) {
        IChatBaseComponent tabheader = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabfooter = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter tablist = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = tablist.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(tablist, tabheader);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = tablist.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(tablist, tabfooter);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tablist);
        }

    }

    private void setPermissions(Player player) {
        final PermissionAttachment attachment = player.addAttachment(SpigotPlugin.getSpigotPlugin());

        attachment.getPermissions().keySet().forEach((perm) -> attachment.setPermission(perm, false));

        ArrayList<String> perms = new ArrayList<>();

        BloodPlayer bp = new BloodPlayer(player.getName());
        if(!bp.exists())
            bp.createDefault();

        bp.getPermissionGroups().forEach(group -> {
            PermissionGroup permGroup = new PermissionGroup(group);
            if(!permGroup.exists())
                bp.removePermissionGroup(group);
            else
                perms.addAll(permGroup.getSpigotPerms());
        });

        perms.forEach((perm) -> {
            if(perm.equalsIgnoreCase("BloodMC.OP"))
                player.setOp(true);
            else
                attachment.setPermission(perm, true);
        });
    }

}
