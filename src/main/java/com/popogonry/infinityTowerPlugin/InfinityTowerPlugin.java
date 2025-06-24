package com.popogonry.infinityTowerPlugin;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.Area.AreaEvent;
import com.popogonry.infinityTowerPlugin.InfinityTower.*;
import com.popogonry.infinityTowerPlugin.Monster.Monster;
import com.popogonry.infinityTowerPlugin.Monster.MonsterRepository;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import com.popogonry.infinityTowerPlugin.ScoreHologram.ScoreHologram;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBox;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxEvent;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
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
        ConfigurationSerialization.registerClass(ScoreHologram.class);

        getServer().getPluginManager().registerEvents(new AreaEvent(), this);
        getServer().getPluginManager().registerEvents(new InfinityTowerEvent(), this);
        getServer().getPluginManager().registerEvents(new StorageBoxEvent(), this);
        getServer().getPluginCommand("it").setExecutor(new InfinityTowerCommand());
        getServer().getPluginCommand("무한의탑").setExecutor(new InfinityTowerKoreanCommand());

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Data Load Start...");

        PluginRepository pluginRepository = new PluginRepository();
        pluginRepository.loadPluginDataConfig();

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
        infinityTowerRepository.loadAllInfinityTower();

        RewardRepository rewardRepository = new RewardRepository();
        rewardRepository.loadAllReward();

        MonsterRepository monsterRepository = new MonsterRepository();
        monsterRepository.loadAllMonster();

        StorageBoxRepository storageBoxRepository = new StorageBoxRepository();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            storageBoxRepository.loadStorageBox(onlinePlayer.getUniqueId());
        }

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Data Load Complete!");



        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Plugin Enabled (Developer: PopoGonry)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (InfinityTowerProcess value : InfinityTowerRepository.infinityTowerPlayerHashMap.values()) {
            value.stop();
        }

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Data Store Start...");

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
        infinityTowerRepository.storeAllInfinityTower();

        MonsterRepository monsterRepository = new MonsterRepository();
        monsterRepository.storeAllMonster();

        RewardRepository rewardRepository = new RewardRepository();
        rewardRepository.storeAllReward();

        StorageBoxRepository storageBoxRepository = new StorageBoxRepository();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            storageBoxRepository.storeStorageBox(onlinePlayer.getUniqueId());
        }

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Data Store Complete!");


        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Plugin Disabled (Developer: PopoGonry)");
    }

    public static InfinityTowerPlugin getServerInstance() {
        return serverInstance;
    }
}
