package com.popogonry.infinityTowerPlugin.RoundRecord;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoundRecord implements ConfigurationSerializable {
    private final UUID playerUUID;
    private final String playerName;
    private final int round;
    private final LocalDateTime clearDateTime;

    public RoundRecord(UUID playerUUID, String playerName, int round) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.round = round;
        this.clearDateTime = LocalDateTime.now();
    }

    public RoundRecord(UUID playerUUID, String playerName, int round, LocalDateTime clearDateTime) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.round = round;
        this.clearDateTime = clearDateTime;
    }

    public boolean compareTo(RoundRecord roundRecord) {
        if(roundRecord == null) return false;
        if(this.round < roundRecord.round) return true;
        return false;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getRound() {
        return round;
    }

    public LocalDateTime getClearDateTime() {
        return clearDateTime;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("playerUUID", playerUUID.toString());
        map.put("playerName", playerName);
        map.put("round", round);
        map.put("clearDateTime", clearDateTime.toString()); // ISO-8601 형식
        return map;
    }

    public static RoundRecord deserialize(Map<String, Object> map) {
        UUID playerUUID = UUID.fromString((String) map.get("playerUUID"));
        String playerName = (String) map.get("playerName");
        int round = (int) map.get("round");
        LocalDateTime clearDateTime = LocalDateTime.parse((String) map.get("clearDateTime"));
        return new RoundRecord(playerUUID, playerName, round, clearDateTime);
    }
}
