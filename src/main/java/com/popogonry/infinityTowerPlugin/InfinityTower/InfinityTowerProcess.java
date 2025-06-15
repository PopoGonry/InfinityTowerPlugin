package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.TowerNotWorkingException;
import com.popogonry.infinityTowerPlugin.InfinityTowerPlugin;
import com.popogonry.infinityTowerPlugin.Monster.Monster;
import com.popogonry.infinityTowerPlugin.Monster.MonsterService;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InfinityTowerProcess {

    private final Player player;
    private final InfinityTower infinityTower;
    private int round;
    private int score;
    private boolean isWorking;

    private List<LivingEntity> spawnedMinecraftMobs;
    private List<ActiveMob> spawnedMysticMobs;

    private MonsterService monsterService = new MonsterService();

    public InfinityTowerProcess(Player player, InfinityTower infinityTower) {
        this.player = player;
        this.infinityTower = infinityTower;
        this.round = 0;
    }
    public void start() {
        if(!infinityTower.isWorking()) throw new TowerNotWorkingException(infinityTower.getName());

        // 무한의 탑!
        isWorking = true;





        round++;
        infinityTowerCycle();
    }


    public int infinityTowerCycle() {
        while(isWorking) {
            List<List<Monster>> spawnMonsters = monsterService.getSpawnMonsters(round);
            spawnedMinecraftMobs = new ArrayList<>();
            spawnedMysticMobs = new ArrayList<>();

            for (List<Monster> spawnMonster : spawnMonsters) {
                for (Monster monster : spawnMonster) {
                    Location monsterSpawnLocationInArea = monsterService.getMonsterSpawnLocationInArea(infinityTower.getArea());
                    if(monster.isMysticMob()) {
                        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(monster.getName()).orElse(null);
                        if(mob != null){
                            ActiveMob activeMob = mob.spawn(BukkitAdapter.adapt(monsterSpawnLocationInArea),1);
                            spawnedMysticMobs.add(activeMob);
                        }
                    }
                    else {

                    }
                }
                Bukkit.getScheduler().runTaskLater(InfinityTowerPlugin.getServerInstance(), () -> {
                }, 20L * PluginRepository.pluginConfig.getMobSpawnDelaySeconds()); // 20L = 1초 → 40L = 2초
            }



        }
    }


}
