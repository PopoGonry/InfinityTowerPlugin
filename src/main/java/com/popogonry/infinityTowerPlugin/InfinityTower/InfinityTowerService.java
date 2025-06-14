package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.Area.Exception.LocationOutsideAreaException;
import com.popogonry.infinityTowerPlugin.Area.Exception.AreaNotCompleteException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.NameNotFoundException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.UUIDAlreadyExistsException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class InfinityTowerService {



    public InfinityTower createInfinityTower(String name, Area area, double[] spawnLocation) {
        UUID uuid;
        try {
            uuid = nameToUUID(name);
        } catch (NameNotFoundException e) {
            uuid = UUID.randomUUID();
        }

        if(InfinityTowerRepository.infinityTowerUUIDSet.contains(uuid)) {
            throw new UUIDAlreadyExistsException(uuid);
        }

        InfinityTower infinityTower = new InfinityTower(uuid, name, area, spawnLocation, false);

        InfinityTowerRepository.infinityTowerHashMap.put(uuid, infinityTower);
        InfinityTowerRepository.infinityTowerUUIDSet.add(uuid);

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
        infinityTowerRepository.saveInfinityTowerSet();
        infinityTowerRepository.saveInfinityTower(uuid);

        return infinityTower;
    }

    public InfinityTower updateInfinityTower(String name, Area area, double[] spawnLocation) throws NameNotFoundException {
        UUID uuid;
        uuid = nameToUUID(name);

        InfinityTower infinityTower = InfinityTowerRepository.infinityTowerHashMap.get(uuid);

        infinityTower.setArea(area == null ? infinityTower.getArea() : area);
        infinityTower.setSpawnLocation(spawnLocation == null ? infinityTower.getSpawnLocation() : spawnLocation);

        infinityTower.setWorking(false);

        InfinityTowerRepository.infinityTowerHashMap.put(uuid, infinityTower);

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
        infinityTowerRepository.saveInfinityTower(uuid);

        return infinityTower;
    }

    public void deleteInfinityTower(String name) throws NameNotFoundException {
        UUID uuid;
        uuid = nameToUUID(name);

        InfinityTowerRepository.infinityTowerHashMap.remove(uuid);
        InfinityTowerRepository.infinityTowerUUIDSet.remove(uuid);

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();

        infinityTowerRepository.saveInfinityTowerSet();
        infinityTowerRepository.removeInfinityTower(uuid);
    }

    public void onInfinityTower(String name) {
        UUID uuid;
        try {
            uuid = nameToUUID(name);
        } catch (NameNotFoundException e) {
            throw e;
        }

        InfinityTower infinityTower = InfinityTowerRepository.infinityTowerHashMap.get(uuid);

        Area area = infinityTower.getArea() != null ? infinityTower.getArea() : new Area();

        // Area 값이 없을 때,
        if(!area.isComplete()) {
            throw new AreaNotCompleteException(area);
        }

        double[] spawnLocation = infinityTower.getSpawnLocation() != null ? infinityTower.getSpawnLocation() : new double[0];

        // 스폰 좌표가 Area 밖 일때,
        if(!area.isInside(spawnLocation)) {
            throw new LocationOutsideAreaException(spawnLocation);
        }

        infinityTower.setWorking(true);

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
        infinityTowerRepository.saveInfinityTower(uuid);
    }

    public void offInfinityTower(String name) throws NameNotFoundException {
        UUID uuid;
        uuid = nameToUUID(name);

        InfinityTower infinityTower = InfinityTowerRepository.infinityTowerHashMap.get(uuid);

        infinityTower.setWorking(false);

        InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
        infinityTowerRepository.saveInfinityTower(uuid);
    }

    public void printInfinityTowers(CommandSender sender) {
        for (UUID uuid : InfinityTowerRepository.infinityTowerHashMap.keySet()) {
            InfinityTower infinityTower = InfinityTowerRepository.infinityTowerHashMap.get(uuid);
            sender.sendMessage(infinityTower.toString());
        }
    }


    public UUID nameToUUID(String name) {
        for (UUID uuid : InfinityTowerRepository.infinityTowerHashMap.keySet()) {
            if(InfinityTowerRepository.infinityTowerHashMap.get(uuid).getName().equals(name)) {
                return uuid;
            }
        }
        throw new NameNotFoundException(name);
    }
}
