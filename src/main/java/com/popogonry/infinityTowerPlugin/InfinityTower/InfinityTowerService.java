package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.NameAlreadyExistsException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.NameNotFoundException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.UUIDAlreadyExistsException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.UUIDNotFoundException;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class InfinityTowerService {


    public InfinityTower createInfinityTower(String name, Area area, double[] spawnLocation, List<ItemStack> rewardList) {
        UUID uuid;
        try {
            uuid = nameToUUID(name);
        } catch (NameNotFoundException e) {
            uuid = UUID.randomUUID();
        }

        if(InfinityTowerRepository.infinityTowerUUIDSet.contains(uuid)) {
            throw new UUIDAlreadyExistsException(uuid);
        }

        InfinityTower infinityTower = new InfinityTower(uuid, name, area, spawnLocation, false, rewardList);

        InfinityTowerRepository.infinityTowerHashMap.put(uuid, infinityTower);
        InfinityTowerRepository.infinityTowerUUIDSet.add(uuid);

        return infinityTower;
    }

    public InfinityTower updateInfinityTower(String name, Area area, double[] spawnLocation, List<ItemStack> rewardList) throws NameNotFoundException {
        UUID uuid;
        uuid = nameToUUID(name);

        InfinityTower infinityTower = InfinityTowerRepository.infinityTowerHashMap.get(uuid);

        infinityTower.setArea(area == null ? infinityTower.getArea() : area);
        infinityTower.setSpawnLocation(spawnLocation == null ? infinityTower.getSpawnLocation() : spawnLocation);
        infinityTower.setRewardList(rewardList == null ? infinityTower.getRewardList() : rewardList);

        return infinityTower;
    }

    public void deleteInfinityTower(UUID uuid) {
        if(!InfinityTowerRepository.infinityTowerUUIDSet.contains(uuid)) {
            throw new UUIDNotFoundException(uuid);
        }

        InfinityTowerRepository.infinityTowerHashMap.remove(uuid);
        InfinityTowerRepository.infinityTowerUUIDSet.remove(uuid);
    }

    public UUID nameToUUID(String name) {
        for (UUID uuid : InfinityTowerRepository.infinityTowerHashMap.keySet()) {
            if(InfinityTowerRepository.infinityTowerHashMap.get(uuid).equals(name)) {
                return uuid;
            }
        }
        throw new NameNotFoundException(name);
    }
}
