package com.popogonry.infinityTowerPlugin.Ranking;


import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecordDataConfig;
import org.bukkit.OfflinePlayer;

public class RankingRepository {

    private final String configBasePath;
    private static final String RECORDS_FILE_NAME = "ranking.yml";

    private final RankingDataConfig dataConfig;

    public static Ranking ranking = new Ranking();

    public RankingRepository() {
        this.configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new RankingDataConfig(configBasePath, RECORDS_FILE_NAME);
    }

    public void reloadConfig() {
        dataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
    }

    public boolean hasRanking() {
        return dataConfig.hasRankingData();
    }

    public void storeRanking() {
        dataConfig.storeRankingData(ranking);
        ranking = new Ranking();
    }

    public void saveRanking() {
        dataConfig.storeRankingData(ranking);
    }

    public void loadRanking() {
        ranking = dataConfig.loadRankingData();
    }

    public void removeRanking() {
        dataConfig.removeRankingData();
    }
}
