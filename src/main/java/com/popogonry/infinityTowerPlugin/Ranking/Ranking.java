package com.popogonry.infinityTowerPlugin.Ranking;

import com.popogonry.infinityTowerPlugin.RoundRecord.RoundRecord;
import io.lumine.mythic.bukkit.utils.lib.jooq.Loader;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.time.LocalDateTime;
import java.util.*;

public class Ranking implements ConfigurationSerializable {
    private static final Comparator<RoundRecord> comparator = Comparator.comparingInt(RoundRecord::getRound);


    public final HashMap<String, PriorityQueue<RoundRecord>> rankingHashMap = new HashMap<>();
    public final HashMap<String, LocalDateTime> lastUpdateDateTimeHashMap = new HashMap<>();

    public Ranking(PriorityQueue<RoundRecord> dailyRanking, PriorityQueue<RoundRecord> weeklyRanking, PriorityQueue<RoundRecord> monthlyRanking, LocalDateTime dailyLastUpdateDateTime, LocalDateTime weeklyLastUpdateDateTime, LocalDateTime monthlyLastUpdateDateTime) {
        PriorityQueue<RoundRecord> dailyRankingCopy = new PriorityQueue<>(comparator);
        dailyRankingCopy.addAll(dailyRanking);
        rankingHashMap.put("daily", dailyRankingCopy);

        PriorityQueue<RoundRecord> weeklyRankingCopy = new PriorityQueue<>(comparator);
        dailyRankingCopy.addAll(weeklyRankingCopy);
        rankingHashMap.put("weekly", weeklyRankingCopy);

        PriorityQueue<RoundRecord> monthlyRankingCopy = new PriorityQueue<>(comparator);
        dailyRankingCopy.addAll(monthlyRankingCopy);
        rankingHashMap.put("monthly", monthlyRankingCopy);

        lastUpdateDateTimeHashMap.put("daily", dailyLastUpdateDateTime);
        lastUpdateDateTimeHashMap.put("weekly", weeklyLastUpdateDateTime);
        lastUpdateDateTimeHashMap.put("monthly", monthlyLastUpdateDateTime);
    }

    public Ranking() {
        rankingHashMap.put("daily", new PriorityQueue<>(comparator));
        rankingHashMap.put("weekly", new PriorityQueue<>(comparator));
        rankingHashMap.put("monthly", new PriorityQueue<>(comparator));

        lastUpdateDateTimeHashMap.put("daily", LocalDateTime.now());
        lastUpdateDateTimeHashMap.put("weekly", LocalDateTime.now());
        lastUpdateDateTimeHashMap.put("monthly", LocalDateTime.now());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, PriorityQueue<RoundRecord>> entry : rankingHashMap.entrySet()) {
            map.put(entry.getKey() + "_ranking", new ArrayList<>(entry.getValue()));
        }

        for (Map.Entry<String, LocalDateTime> entry : lastUpdateDateTimeHashMap.entrySet()) {
            map.put(entry.getKey() + "_lastUpdate", entry.getValue().toString());
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public static Ranking deserialize(Map<String, Object> map) {
        Ranking ranking = new Ranking();

        for (String key : Arrays.asList("daily", "weekly", "monthly")) {
            // 랭킹
            Object rawList = map.get(key + "_ranking");
            if (rawList instanceof List<?>) {
                PriorityQueue<RoundRecord> queue = new PriorityQueue<>(comparator);
                for (Object obj : (List<?>) rawList) {
                    if (obj instanceof RoundRecord record) {
                        queue.add(record);
                    } else if (obj instanceof Map<?, ?> recordMap) {
                        queue.add(RoundRecord.deserialize((Map<String, Object>) recordMap));
                    }
                }
                ranking.rankingHashMap.put(key, queue);
            }

            // 마지막 업데이트
            Object rawTime = map.get(key + "_lastUpdate");
            if (rawTime instanceof String timeStr) {
                ranking.lastUpdateDateTimeHashMap.put(key, LocalDateTime.parse(timeStr));
            }
        }

        return ranking;
    }

    public void printRanking() {
        System.out.println("========== 랭킹 출력 ==========");

        for (String period : Arrays.asList("daily", "weekly", "monthly")) {
            System.out.println("[" + period.toUpperCase() + "]");

            PriorityQueue<RoundRecord> queue = rankingHashMap.get(period);
            if (queue == null || queue.isEmpty()) {
                System.out.println(" - 랭킹 없음");
            } else {
                // 정렬 보존 안되므로 복사 후 정렬
                List<RoundRecord> sorted = new ArrayList<>(queue);
                sorted.sort(comparator.reversed()); // 높은 점수 순으로 보기 좋게

                int rank = 1;
                for (RoundRecord record : sorted) {
                    System.out.println(" " + rank++ + ". " + record.getPlayerName() + " - " + record.getRound() + "층 (" + record.getClearDateTime() + ")");
                }
            }

            LocalDateTime lastUpdate = lastUpdateDateTimeHashMap.get(period);
            System.out.println(" - 마지막 업데이트: " + (lastUpdate != null ? lastUpdate : "알 수 없음"));
            System.out.println();
        }

        System.out.println("=================================");
    }

}
