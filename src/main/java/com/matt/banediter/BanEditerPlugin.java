package com.matt.banediter;

import org.bukkit.plugin.java.JavaPlugin;

public class BanEditerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Matts Custom Ban Editer enabled!");

        // Register command
        getCommand("editban").setExecutor(new BanCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Matts Custom Ban Editer disabled!");
    }
}
