
package com.popogonry.infinityTowerPlugin;

import com.popogonry.infinityTowerPlugin.InfinityTower.InfinityTowerRepository;
import com.popogonry.infinityTowerPlugin.Monster.MonsterRepository;
import com.popogonry.infinityTowerPlugin.Ranking.RankingRepository;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxRepository;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramRepository;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PluginRepository {
    private static final String CONFIG_FILE_NAME = "config.yml";
    private final String configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
    private final PluginDataConfig pluginDataConfig;
    public static PluginConfig pluginConfig;

    public PluginRepository() {
        this.pluginDataConfig = new PluginDataConfig(this.configBasePath, "config.yml");
    }

    public void reloadConfig() {
        this.pluginDataConfig.reload();
    }

    public void saveConfig() {
        this.pluginDataConfig.store();
    }

    public void loadPluginDataConfig() {
        pluginConfig = this.pluginDataConfig.loadPluginDataConfig();
    }

    public static void loadAllData() {
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

        RankingRepository rankingRepository = new RankingRepository();
        rankingRepository.loadRanking();

        TextDisplayHologramRepository textDisplayHologramRepository = new TextDisplayHologramRepository();
        textDisplayHologramRepository.loadTextDisplayHologram();

        TextDisplayHologramService textDisplayHologramService = new TextDisplayHologramService();
        textDisplayHologramService.settingHologram();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Data Load Complete!");
    }

    public static void saveAllData() {
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Data Store Start...");

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
        infinityTowerRepository.saveAllInfinityTower();

        MonsterRepository monsterRepository = new MonsterRepository();
        monsterRepository.saveAllMonster();

        RewardRepository rewardRepository = new RewardRepository();
        rewardRepository.saveAllReward();

        StorageBoxRepository storageBoxRepository = new StorageBoxRepository();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            storageBoxRepository.saveStorageBox(onlinePlayer.getUniqueId());
        }

        RankingRepository rankingRepository = new RankingRepository();
        rankingRepository.saveRanking();

        TextDisplayHologramService textDisplayHologramService = new TextDisplayHologramService();
        textDisplayHologramService.clearHologram();

        TextDisplayHologramRepository textDisplayHologramRepository = new TextDisplayHologramRepository();
        textDisplayHologramRepository.saveTextDisplayHologram();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "InfinityTower Data Store Complete!");
    }
}
