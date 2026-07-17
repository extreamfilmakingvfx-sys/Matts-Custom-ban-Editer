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

        // Console ALWAYS bypasses OP immunity
        boolean isConsole = !(sender instanceof org.bukkit.entity.Player);

        if (!isConsole && !sender.hasPermission("banediter.edit")) {
            sender.sendMessage(plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("Usage: /editban <player> <prefix> <reason>");
            return true;
        }

        String playerName = args[0];
        String prefix = args[1];
        String reason = String.join(" ", args).replace(playerName + " " + prefix + " ", "");

        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry entry = banList.getBanEntry(playerName);

        // If player is not banned yet, create a new ban entry
        if (entry == null) {
            entry = banList.addBan(playerName, reason, null, sender.getName());
        }

        // Build custom ban message
        String format = plugin.getConfig().getString("ban-format");
        String customMessage = format
                .replace("{prefix}", prefix)
                .replace("{player}", playerName)
                .replace("{reason}", reason);

        // Override default ban screen
        if (plugin.getConfig().getBoolean("override-ban-screen")) {
            entry.setReason(customMessage);
        } else {
            entry.setReason(reason);
        }

        // Force apply ban even if OP has bypass permissions
        banList.addBan(entry.getTarget(), entry.getReason(), entry.getExpiration(), sender.getName());

        sender.sendMessage(
                plugin.getConfig().getString("messages.ban-updated")
                        .replace("{player}", playerName)
        );

        return true;
    }
}
