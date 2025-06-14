package com.popogonry.infinityTowerPlugin;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.Area.AreaEvent;
import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTower;
import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTowerCommand;
import com.popogonry.infinityTowerPlugin.Monster.Monster;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class InfinityTowerPlugin extends JavaPlugin {

    private static InfinityTowerPlugin serverInstance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        serverInstance = this;

        ConfigurationSerialization.registerClass(InfinityTower.class);
        ConfigurationSerialization.registerClass(Area.class);
        ConfigurationSerialization.registerClass(Monster.class);

        getServer().getPluginManager().registerEvents(new AreaEvent(), this);


        getServer().getPluginCommand("it").setExecutor(new InfinityTowerCommand());





        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Plugin Enabled (Developer: PopoGonry)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic





        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Plugin Disabled (Developer: PopoGonry)");
    }

    public static InfinityTowerPlugin getServerInstance() {
        return serverInstance;
    }
}
