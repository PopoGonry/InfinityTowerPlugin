package com.popogonry.infinityTowerPlugin.RoundRecord;

import com.popogonry.infinityTowerPlugin.Config;
import org.bukkit.OfflinePlayer;

public class RoundRecordDataConfig extends Config {
    public RoundRecordDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
    }

    public void storeRecordData(OfflinePlayer offlinePlayer, RoundRecord roundRecord) {
        getConfig().set(String.valueOf(offlinePlayer.getUniqueId()), roundRecord);
        super.store();
    }

    public RoundRecord loadRecordData(OfflinePlayer offlinePlayer) {
        return (RoundRecord) getConfig().get(String.valueOf(offlinePlayer.getUniqueId()));
    }

    public boolean hasRecordData(OfflinePlayer offlinePlayer) {
        return getConfig().contains(String.valueOf(offlinePlayer.getUniqueId()));
    }

    public void removeRecordData(OfflinePlayer offlinePlayer) {
        getConfig().set(String.valueOf(offlinePlayer.getUniqueId()), null);
        super.store();
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {

    }
}
