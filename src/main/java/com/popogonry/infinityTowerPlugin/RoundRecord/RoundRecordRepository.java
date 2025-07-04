package com.popogonry.infinityTowerPlugin.RoundRecord;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import org.bukkit.OfflinePlayer;

public class RoundRecordRepository {

    private final String configBasePath;
    private static final String RECORDS_FILE_NAME = "records.yml";

    private final RoundRecordDataConfig dataConfig;

    public RoundRecordRepository() {
        this.configBasePath = InfinityTowerPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new RoundRecordDataConfig(configBasePath, RECORDS_FILE_NAME);
    }

    public void reloadConfig() {
        dataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
    }

    public boolean hasRecord(OfflinePlayer offlinePlayer) {
        return dataConfig.hasRecordData(offlinePlayer);
    }

    public void storeRecord(OfflinePlayer offlinePlayer, RoundRecord roundRecord) {
        dataConfig.storeRecordData(offlinePlayer, roundRecord);
    }

    public RoundRecord loadRecord(OfflinePlayer offlinePlayer) {
        return dataConfig.loadRecordData(offlinePlayer);
    }

    public void removeRecord(OfflinePlayer offlinePlayer) {
        dataConfig.removeRecordData(offlinePlayer);
    }
}
