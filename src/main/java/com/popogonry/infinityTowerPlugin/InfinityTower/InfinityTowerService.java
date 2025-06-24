package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.Area.Exception.LocationOutsideAreaException;
import com.popogonry.infinityTowerPlugin.Area.Exception.AreaNotCompleteException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.*;
import com.popogonry.infinityTowerPlugin.Reference;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InfinityTowerService {



    public InfinityTower createInfinityTower(String name, Area area, double[] spawnLocation) {
        UUID uuid;
        try {
            uuid = nameToUUID(name);
        } catch (NameNotFoundException e) {
            uuid = UUID.randomUUID();
        }

        if(InfinityTowerRepository.infinityTowerUUIDSet.contains(uuid)) {
            throw new UUIDAlreadyExistsException("같은 UUID의 무한의 탑이 존재합니다.");
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
            throw new AreaNotCompleteException("구역 설정이 완료되지 않았습니다.");
        }

        double[] spawnLocation = infinityTower.getSpawnLocation() != null ? infinityTower.getSpawnLocation() : new double[0];

        // 스폰 좌표가 Area 밖 일때,
        if(!area.isInside(spawnLocation)) {
            throw new LocationOutsideAreaException("스폰이 구역 안에 존재하지 않습니다.");
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
        throw new NameNotFoundException(name + "의 무한의 탑을 찾을 수 없습니다.");
    }

    public ItemStack getInfinityTowerTicket() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Reference.prefix_normal + "Infinity Tower Ticket");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public UUID getEnterableTowerUUID() {

        HashMap<UUID, InfinityTower> towers = new HashMap<>(InfinityTowerRepository.infinityTowerHashMap);

        for (UUID uuid : InfinityTowerRepository.infinityTowerHashMap.keySet()) {
            if(!InfinityTowerRepository.infinityTowerHashMap.get(uuid).isWorking()) towers.remove(uuid);
        }

        for (InfinityTowerProcess value : InfinityTowerRepository.infinityTowerPlayerHashMap.values()) {
            towers.remove(value.getInfinityTower().getId());
        }

        if(towers.isEmpty()) {
            throw new EnterableTowerNotExistsException("입장 가능한 무한의 탑이 없습니다.");
        }

        return towers.keySet().iterator().next();
    }


    public void enterInfinityTower(UUID uuid, Player player) {
        // uuid 타워가 없을 때,
        if(!InfinityTowerRepository.infinityTowerHashMap.containsKey(uuid)) throw new UUIDNotFoundException(uuid + " 무한의 탑은 존재하지 않습니다.");

        // player가 이미 타워에 있을 때,
        if(InfinityTowerRepository.infinityTowerPlayerHashMap.containsKey(player)) throw new PlayerInTowerException("이미 무한의 탑을 진행 중입니다.");

        // 다른 플레이어가 타워에 들어가 있을 때,
        for (InfinityTowerProcess value : InfinityTowerRepository.infinityTowerPlayerHashMap.values()) {
            if(value.getInfinityTower().getId().equals(uuid)) throw new PlayerInTowerException("다른 플레이어가 무한의 탑을 진행 중입니다.");
        }

        InfinityTower infinityTower = InfinityTowerRepository.infinityTowerHashMap.get(uuid);

        // 타워가 점검 중 일때,
        if (!infinityTower.isWorking()) throw new TowerNotWorkingException("무한의 탑이 비활성화 상태입니다.");

        InfinityTowerProcess infinityTowerProcess = new InfinityTowerProcess(player, infinityTower);
        infinityTowerProcess.start();
    }

    public boolean killProcess(UUID uuid) {
        // uuid 타워가 없을 때,
        if(!InfinityTowerRepository.infinityTowerHashMap.containsKey(uuid)) throw new UUIDNotFoundException(uuid + " 무한의 탑은 존재하지 않습니다.");

        List<Player> list = new ArrayList<>(InfinityTowerRepository.infinityTowerPlayerHashMap.keySet());

        for (Player player : list) {
            if(InfinityTowerRepository.infinityTowerPlayerHashMap.get(player).getInfinityTower().getId().equals(uuid)) {
                InfinityTowerRepository.infinityTowerPlayerHashMap.get(player).stop();
                return true;
            }
        }
        return false;
    }

}
