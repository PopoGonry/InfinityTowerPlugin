package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.Area;
import com.popogonry.infinityTowerPlugin.Area.AreaRepository;
import com.popogonry.infinityTowerPlugin.Area.AreaService;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.NameNotFoundException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.UUIDAlreadyExistsException;
import com.popogonry.infinityTowerPlugin.Monster.Monster;
import com.popogonry.infinityTowerPlugin.Monster.MonsterRepository;
import com.popogonry.infinityTowerPlugin.Monster.MonsterService;
import com.popogonry.infinityTowerPlugin.Reference;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class InfinityTowerCommand implements CommandExecutor {
    AreaService areaService = new AreaService();
    InfinityTowerService infinityTowerService = new InfinityTowerService();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return false;
        }
        Player player = (Player) sender;


        if(!sender.isOp()) {
            sender.sendMessage(Reference.prefix_error + "op 전용 명령어입니다.");
            return false;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("test")) {
                UUID uuid = UUID.randomUUID();

                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    list.add(i);
                }

                for(int i = 1; i <= 5; i++) {
                    uuid = UUID.randomUUID();
                    MonsterRepository.monsterHashMap.put(uuid, new Monster(uuid, "name" + i, false, 30 * i, list));
                    MonsterRepository.monsterUUIDSet.add(uuid);
                }

            }if(args[0].equalsIgnoreCase("test2")) {
                MonsterService monsterService = new MonsterService();

//                Monster monster = monsterService.calculateRoundMonster(10, 100);
                List<List<Monster>> spawnMonsters = monsterService.getSpawnMonsters(10);

                System.out.println(spawnMonsters);
            }
            else if(args[0].equalsIgnoreCase("load")) {
                InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
                infinityTowerRepository.loadAllInfinityTower();

                MonsterRepository monsterRepository = new MonsterRepository();
                monsterRepository.loadMonsterSet();

                RewardRepository rewardRepository = new RewardRepository();
                rewardRepository.loadAllReward();
            }
            else if(args[0].equalsIgnoreCase("save")) {
                InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
                infinityTowerRepository.saveAllInfinityTower();

                MonsterRepository monsterRepository = new MonsterRepository();
                monsterRepository.saveAllMonster();

                RewardRepository rewardRepository = new RewardRepository();
                rewardRepository.saveAllReward();
            }
//            else if(args[0].equalsIgnoreCase("store")) {
//
//            }
            else if (args[0].equalsIgnoreCase("list")) {

                player.sendMessage(MonsterRepository.monsterHashMap.values().toString());
                player.sendMessage(MonsterRepository.monsterUUIDSet.toString());

                infinityTowerService.printInfinityTowers(sender);

            }
            else if (args[0].equalsIgnoreCase("areaOn")) {

                AreaService areaService = new AreaService();
                areaService.onAreaSettingMode(((Player) sender).getPlayer());

            }
            else if (args[0].equalsIgnoreCase("areaOff")) {

                areaService.offAreaSettingMode(((Player) sender));

            }
            else if (args[0].equalsIgnoreCase("wand")) {
                player.getInventory().addItem(areaService.getAreaSettingTool());
            }


        }
        else if(args.length == 2) {

            if(args[0].equalsIgnoreCase("create")) {
                try {
                    infinityTowerService.createInfinityTower(args[1], null, null);
                } catch (UUIDAlreadyExistsException e) {
                    sender.sendMessage(e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("areaSet")) {
                try {
                    infinityTowerService.updateInfinityTower(args[1], AreaRepository.playerAreaHashMap.getOrDefault(player, null), null);
                } catch (NameNotFoundException e) {
                    sender.sendMessage(e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("spawnSet")) {
                double[] location = new double[3];
                location[0] = player.getLocation().getX();
                location[1] = player.getLocation().getY();
                location[2] = player.getLocation().getZ();

                try {
                    infinityTowerService.updateInfinityTower(args[1], null, location);
                } catch (NameNotFoundException e) {
                    sender.sendMessage(e.getMessage());
                }

            }
            else if(args[0].equalsIgnoreCase("remove")) {
                try {
                    infinityTowerService.deleteInfinityTower(args[1]);
                } catch (NameNotFoundException e) {
                    sender.sendMessage(e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("on")) {
                try {
                    infinityTowerService.onInfinityTower(args[1]);
                } catch (Exception e) {
                    sender.sendMessage(e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("off")) {
                try {
                    infinityTowerService.offInfinityTower(args[1]);
                } catch (NameNotFoundException e) {
                    sender.sendMessage(e.getMessage());
                }
            }
            if(args[0].equalsIgnoreCase("test3")) {
                MonsterService monsterService = new MonsterService();

//                Monster monster = monsterService.calculateRoundMonster(10, 100);
//                List<List<Monster>> spawnMonsters = monsterService.getSpawnMonsters(10);
                Monster monster = monsterService.calculateRoundMonster(10, Integer.parseInt(args[1]));

                System.out.println(monster);
            }

        }





        return false;
    }
}
