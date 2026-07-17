package com.matt.banediter;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BanCommand implements CommandExecutor {

    private final BanEditerPlugin plugin;

    public BanCommand(BanEditerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("banediter.edit")) {
            sender.sendMessage(plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("Usage: /editban <player> <new message>");
            return true;
        }

        String playerName = args[0];
        String newMessage = String.join(" ", args).replace(playerName + " ", "");

        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry entry = banList.getBanEntry(playerName);

        if (entry == null) {
            sender.sendMessage(plugin.getConfig().getString("messages.player-not-found"));
            return true;
        }

        entry.setReason(newMessage);
        banList.addBan(entry.getTarget(), entry.getReason(), entry.getExpiration(), entry.getSource());

        sender.sendMessage(plugin.getConfig().getString("messages.ban-updated").replace("%player%", playerName));
        return true;
    }
}
