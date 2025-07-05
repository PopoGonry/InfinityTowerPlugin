package com.popogonry.infinityTowerPlugin;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.Area.AreaEvent;
import com.popogonry.infinityTowerPlugin.InfinityTower.*;
import com.popogonry.infinityTowerPlugin.Monster.Monster;
import com.popogonry.infinityTowerPlugin.Monster.MonsterRepository;
import com.popogonry.infinityTowerPlugin.Ranking.Ranking;
import com.popogonry.infinityTowerPlugin.Ranking.RankingRepository;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBox;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxEvent;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxRepository;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramLocation;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramRepository;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class InfinityTowerPlugin extends JavaPlugin {

    private static InfinityTowerPlugin serverInstance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        serverInstance = this;
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(StorageBox.class);
        ConfigurationSerialization.registerClass(InfinityTower.class);
        ConfigurationSerialization.registerClass(Area.class);
        ConfigurationSerialization.registerClass(Monster.class);
        ConfigurationSerialization.registerClass(RoundRecord.class);
        ConfigurationSerialization.registerClass(Ranking.class);
        ConfigurationSerialization.registerClass(TextDisplayHologramLocation.class);

        getServer().getPluginManager().registerEvents(new AreaEvent(), this);
        getServer().getPluginManager().registerEvents(new InfinityTowerEvent(), this);
        getServer().getPluginManager().registerEvents(new StorageBoxEvent(), this);
        getServer().getPluginCommand("it").setExecutor(new InfinityTowerCommand());
        getServer().getPluginCommand("무한의탑").setExecutor(new InfinityTowerKoreanCommand());


        PluginRepository.loadAllData();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Plugin Enabled (Developer: PopoGonry)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (InfinityTowerProcess value : InfinityTowerRepository.infinityTowerPlayerHashMap.values()) {
            value.stop();
        }

        PluginRepository.saveAllData();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Plugin Disabled (Developer: PopoGonry)");
    }

    public static InfinityTowerPlugin getServerInstance() {
        return serverInstance;
    }
}
