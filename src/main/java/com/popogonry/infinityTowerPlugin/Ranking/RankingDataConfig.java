package com.popogonry.infinityTowerPlugin.Ranking;

import com.popogonry.infinityTowerPlugin.Config;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import org.bukkit.OfflinePlayer;

public class RankingDataConfig extends Config {

    private static final String dataKey = "Ranking";

    public RankingDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeRankingData(Ranking ranking) {
        getConfig().set(dataKey, ranking);
        super.store();
    }

    public Ranking loadRankingData() {
        Ranking ranking = (Ranking) getConfig().get(dataKey);
        return ranking == null ? new Ranking() : ranking;
    }

    public boolean hasRankingData() {
        return getConfig().contains(dataKey);
    }

    public void removeRankingData() {
        getConfig().set(dataKey, null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
