package com.popogonry.infinityTowerPlugin.RoundRecord;

import org.bukkit.entity.Player;

public class RoundRecordService {

    public boolean record(RoundRecord newRoundRecord, Player player) {
        RoundRecordRepository roundRecordRepository = new RoundRecordRepository();

        RoundRecord oldRoundRecord = roundRecordRepository.loadRecord(player);

        // 새로운 기록이 더 좋다면,
        if(oldRoundRecord == null || oldRoundRecord.compareTo(newRoundRecord)) {
            roundRecordRepository.storeRecord(player, newRoundRecord);
            return true;
        }
        return false;
    }

}
