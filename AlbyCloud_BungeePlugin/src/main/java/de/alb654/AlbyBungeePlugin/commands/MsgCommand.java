package de.alb654.AlbyBungeePlugin.commands;

/*
 * Copyright (c)
 * Created by Aaron Bernert on 2.8.2020
 */

import de.alb654.AlbyBungeePlugin.BungeePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public abstract class MsgCommand extends Command {

    private final ArrayList<String> receiver = new ArrayList<>();

    public MsgCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if(args.length != 1) {
                player.sendMessage(BungeePlugin.Bprefix + "§c/" + getName() + " on/off");
                return;
            }

            String uuid = player.getUniqueId().toString();

            if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("an")) {

                if(!receiver.contains(uuid)) {
                    receiver.add(uuid);
                    player.sendMessage(BungeePlugin.Bprefix + "§7Nachrichten §aaktiviert§7!");
                } else {
                    player.sendMessage(BungeePlugin.Bprefix + "§cDu bekommst bereits Nachrichten!");
                }
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("aus")){
                if(!receiver.contains(uuid)) {
                    player.sendMessage(BungeePlugin.Bprefix + "§cDu bekommst keine Nachrichten!");
                } else {
                    receiver.remove(uuid);
                    player.sendMessage(BungeePlugin.Bprefix + "§7Nachrichten §cdeaktiviert§7!");
                }
            } else
                player.sendMessage(BungeePlugin.Bprefix + "§c/" + getName() + " on/off");
        }
    }

    public ArrayList<String> getReceiver() {
        return receiver;
    }

}
