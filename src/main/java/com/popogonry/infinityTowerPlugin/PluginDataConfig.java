
package com.popogonry.infinityTowerPlugin;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PluginDataConfig extends Config {
    public PluginDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public PluginConfig loadPluginDataConfig() {
        return new PluginConfig(
                this.getConfig().getInt("Round-Score"),
                this.getConfig().getInt("Round-Clear-Seconds"),
                this.getConfig().getStringList("Allowed-Commands")
        );
    }

    public void loadDefaults() {
    }

    public void applySettings() {
        this.getConfig().options().copyDefaults(true);
    }
}
