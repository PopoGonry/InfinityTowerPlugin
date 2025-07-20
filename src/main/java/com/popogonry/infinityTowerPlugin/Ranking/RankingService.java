package com.popogonry.infinityTowerPlugin.Ranking;

import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecordRepository;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramService;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class RankingService {
    private static final Comparator<RoundRecord> comparator = Comparator.comparingInt(RoundRecord::getRound);



    public List<Component> getRankingHologramComponents(String type) {
        MiniMessage mm = MiniMessage.miniMessage();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Component> returnList = new ArrayList<>();

        String titleLine = switch (type) {
            case "daily" -> "<white><bold>============ <yellow>무한의탑 일간 랭킹 <white>============</bold>";
            case "weekly" -> "<white><bold>============ <green>무한의탑 주간 랭킹 <white>============</bold>";
            case "monthly" -> "<white><bold>============ <aqua>무한의탑 월간 랭킹 <white>============</bold>";
            default -> null;
        };
        if (titleLine == null) return null;

        returnList.add(mm.deserialize(titleLine));

        PriorityQueue<RoundRecord> queue = RankingRepository.ranking.rankingHashMap.get(type);

        if (queue == null || queue.isEmpty()) {
            returnList.add(mm.deserialize("<gray> - 랭킹 없음"));
        } else {
            List<RoundRecord> sorted = new ArrayList<>(queue);
            sorted.sort(Comparator.comparingInt(RoundRecord::getRound).reversed());

            int rank = 1;
            for (RoundRecord record : sorted) {
                String line = String.format(
                        "<white><bold>%d.</bold> <gold><bold>%s</bold> <white>- <red><bold>%d층</bold> <gray>(%s)",
                        rank++, record.getPlayerName(), record.getRound(), record.getClearDateTime().format(formatter)
                );
                returnList.add(mm.deserialize(line));
            }
        }

        returnList.add(Component.empty());

        LocalDateTime lastUpdate = RankingRepository.ranking.lastUpdateDateTimeHashMap.get(type);
        String updateLine = "<white><bold>- <gray>마지막 업데이트</gray> : "
                + (lastUpdate != null ? lastUpdate.format(formatter) : "알 수 없음") + "</bold>";
        returnList.add(mm.deserialize(updateLine));

        returnList.add(mm.deserialize("<white>========================================="));

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
        long delayTicks = getTicksUntilNextMidnight(); // 자정까지 대기 시간 계산

        // 자정까지 기다린 후 실행
        new BukkitRunnable() {
            @Override
            public void run() {
                // 첫 실행
                executeCycle();

                // 이후 24시간마다 반복
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        executeCycle();
                    }
                }.runTaskTimerAsynchronously(InfinityTowerPlugin.getServerInstance(), 24 * 60 * 60 * 20L, 24 * 60 * 60 * 20L);
            }
        }.runTaskLater(InfinityTowerPlugin.getServerInstance(), delayTicks);
    }

    private long getTicksUntilNextMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay();
        Duration duration = Duration.between(now, nextMidnight);
        return duration.getSeconds() * 20L; // 초 → 틱
    }

    private void executeCycle() {
        LocalDateTime now = LocalDateTime.now();

        // 일간 랭킹 초기화
        removeRanking("daily");

        // 주간 처리 (매주 월요일)
        if (now.getDayOfWeek() == DayOfWeek.MONDAY) {
            removeRanking("weekly");
        }

        // 월간 처리 (매월 1일)
        if (now.getDayOfMonth() == 1) {
            removeRanking("monthly");
        }

        // 홀로그램 업데이트
        TextDisplayHologramService textDisplayHologramService = new TextDisplayHologramService();
        textDisplayHologramService.updateHologram();
    }

}
