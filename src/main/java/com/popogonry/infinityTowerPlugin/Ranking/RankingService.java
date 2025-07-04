package com.popogonry.infinityTowerPlugin.Ranking;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecordRepository;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class RankingService {
    public boolean addRanking(RoundRecord newRecord) {

        boolean isRankingUpdated = false;

        for (String key : RankingRepository.ranking.rankingHashMap.keySet()) {
            PriorityQueue<RoundRecord> queue = RankingRepository.ranking.rankingHashMap.get(key);

            RoundRecord playerRecord = null;
            for (RoundRecord record : queue) {
                if(record.getPlayerUUID().equals(newRecord.getPlayerUUID())) {
                    playerRecord = record;
                    break;
                }
            }
            if(playerRecord == null) {
                if (queue.size() < 10) {
                    queue.add(newRecord);
                    RankingRepository.ranking.lastUpdateDateTimeHashMap.put(key, LocalDateTime.now());
                    isRankingUpdated = true;
                } else if (newRecord.getRound() > queue.peek().getRound()) {
                    queue.poll();
                    queue.add(newRecord);
                    RankingRepository.ranking.lastUpdateDateTimeHashMap.put(key, LocalDateTime.now());
                    isRankingUpdated = true;
                }
            }
            else {
                if (newRecord.getRound() > playerRecord.getRound()) {
                    queue.remove(playerRecord);
                    queue.add(newRecord);
                    RankingRepository.ranking.lastUpdateDateTimeHashMap.put(key, LocalDateTime.now());
                    isRankingUpdated = true;
                }
            }
        }

        if(isRankingUpdated) {
            RankingRepository rankingRepository = new RankingRepository();
            rankingRepository.saveRanking();
        }

        return isRankingUpdated;
    }

    public void removeRanking(String type) {
        RankingRepository.ranking.rankingHashMap.get(type).clear();
        RankingRepository.ranking.lastUpdateDateTimeHashMap.put(type, LocalDateTime.now());
    }

    public void start() {
        long delayTicks = getTicksUntilNextMidnight();

        new BukkitRunnable() {
            @Override
            public void run() {
                cycle();
            }
        }.runTaskLater(InfinityTowerPlugin.getServerInstance(), delayTicks);
    }

    private long getTicksUntilNextMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay();

        Duration duration = Duration.between(now, nextMidnight);
        long seconds = duration.getSeconds();
        return seconds * 20; // 1초 = 20틱
    }

    public void cycle() {
        long delay = getInitialDelayUntilMidnight();

        // 처음엔 딜레이 후 24시간마다 반복
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();

                // 일간 랭킹 처리
                removeRanking("daily");

                // 주간 처리 (매주 월요일)
                if (now.getDayOfWeek() == DayOfWeek.MONDAY) {
                    removeRanking("weekly");
                }

                // 월간 처리 (매월 1일)
                if (now.getDayOfMonth() == 1) {
                    removeRanking("monthly");
                }
            }
        }.runTaskTimerAsynchronously(InfinityTowerPlugin.getServerInstance(), delay, 24 * 60 * 60 * 20L); // 20L = 1초
    }

    private long getInitialDelayUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay();

        Duration duration = Duration.between(now, nextMidnight);
        return duration.getSeconds() * 20L; // 초 → 틱
    }

}
