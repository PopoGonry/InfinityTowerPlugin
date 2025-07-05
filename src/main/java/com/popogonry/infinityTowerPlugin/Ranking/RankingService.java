package com.popogonry.infinityTowerPlugin.Ranking;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecordRepository;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramService;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RankingService {
    private static final Comparator<RoundRecord> comparator = Comparator.comparingInt(RoundRecord::getRound);

    public List<String> getRankingHologramLines(String type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");

        List<String> returnList = new ArrayList<>();

        String koreanType;
        switch (type) {
            case "daily":
                koreanType = "일간";
                break;
            case "weekly":
                koreanType = "주간";
                break;
            case "monthly":
                koreanType = "월간";
                break;
            default:
                return null;
        }

        returnList.add("========== 무한의탑 " + koreanType + " 랭킹 ==========");

        PriorityQueue<RoundRecord> queue = RankingRepository.ranking.rankingHashMap.get(type);

        if (queue == null || queue.isEmpty()) {
            returnList.add(" - 랭킹 없음");
        } else {
            List<RoundRecord> sorted = new ArrayList<>(queue);
            sorted.sort(comparator.reversed()); // 높은 점수 순으로 보기 좋게

            int rank = 1;
            for (RoundRecord record : sorted) {
                returnList.add(" " + rank++ + ". " + record.getPlayerName() + " - " + record.getRound() + "층 (" + record.getClearDateTime().format(formatter) + ")");
            }
        }
        returnList.add(" ");

        LocalDateTime lastUpdate = RankingRepository.ranking.lastUpdateDateTimeHashMap.get(type);
        returnList.add(" - 마지막 업데이트: " + (lastUpdate != null ? lastUpdate.format(formatter) : "알 수 없음"));

        returnList.add("=================================");

        return returnList;
    }


    public boolean addRanking(RoundRecord newRecord) {

        boolean isRankingUpdated = false;

        for (String key : RankingRepository.ranking.rankingHashMap.keySet()) {
            PriorityQueue<RoundRecord> queue = RankingRepository.ranking.rankingHashMap.get(key);

            RoundRecord playerRecord = null;
            for (RoundRecord record : queue) {
                if (record.getPlayerUUID().equals(newRecord.getPlayerUUID())) {
                    playerRecord = record;
                    break;
                }
            }
            if (playerRecord == null) {
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
            } else {
                if (newRecord.getRound() > playerRecord.getRound()) {
                    queue.remove(playerRecord);
                    queue.add(newRecord);
                    RankingRepository.ranking.lastUpdateDateTimeHashMap.put(key, LocalDateTime.now());
                    isRankingUpdated = true;
                }
            }
        }

        if (isRankingUpdated) {
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
                TextDisplayHologramService textDisplayHologramService = new TextDisplayHologramService();
                textDisplayHologramService.updateHologram();
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
