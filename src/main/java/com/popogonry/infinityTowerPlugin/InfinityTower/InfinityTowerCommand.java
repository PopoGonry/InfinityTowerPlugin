package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.AreaRepository;
import com.popogonry.infinityTowerPlugin.Area.AreaService;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.NameNotFoundException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.UUIDAlreadyExistsException;
import com.popogonry.infinityTowerPlugin.Monster.MonsterRepository;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Ranking.RankingRepository;
import com.popogonry.infinityTowerPlugin.Ranking.RankingService;
import com.popogonry.infinityTowerPlugin.Reference;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import com.popogonry.infinityTowerPlugin.ScoreHologram.HologramUtil;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBox;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxGUI;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxRepository;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InfinityTowerCommand implements CommandExecutor {
    AreaService areaService = new AreaService();
    InfinityTowerService infinityTowerService = new InfinityTowerService();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return false;

        }

        if(!sender.isOp()) {
            sender.sendMessage(Reference.prefix_error + "op 전용 명령어입니다.");
            return false;
        }

        Player player = (Player) sender;

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("test")) {
                HologramUtil.spawnHologram(player.getLocation(), "테스트 문장");

            }else if(args[0].equalsIgnoreCase("test2")) {

                List<String> list = new ArrayList<>();
                list.add("---- 무한의 탑 랭킹 ( 일간 ) ----");
                list.add("- 1. PopoGonry : 230층 -");
                list.add("- 2. PopoGonry : 230층 -");
                list.add("- 3. PopoGonry : 230층 -");
                list.add("- 4. PopoGonry : 230층 -");
                list.add("- 5. PopoGonry : 230층 -");
                list.add("- 6. PopoGonry : 230층 -");
                list.add("- 7. PopoGonry : 230층 -");
                list.add("- 8. PopoGonry : 230층 -");
                list.add("- 9. PopoGonry : 230층 -");


                HologramUtil.spawnHologramLines(player.getLocation(), list);

            } else if(args[0].equalsIgnoreCase("test3")) {
                RankingRepository.ranking.printRanking();


            } else if(args[0].equalsIgnoreCase("test4")) {
                RankingService rankingService = new RankingService();
                rankingService.removeRanking("daily");
            } else if(args[0].equalsIgnoreCase("test5")) {
                RankingService rankingService = new RankingService();
                rankingService.removeRanking("weekly");

            } else if(args[0].equalsIgnoreCase("test6")) {
                RankingService rankingService = new RankingService();
                rankingService.removeRanking("monthly");

            }
            else if(args[0].equalsIgnoreCase("load")) {
                InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
                infinityTowerRepository.loadAllInfinityTower();

                MonsterRepository monsterRepository = new MonsterRepository();
                monsterRepository.loadMonsterSet();

                RewardRepository rewardRepository = new RewardRepository();
                rewardRepository.loadAllReward();

                StorageBoxRepository storageBoxRepository = new StorageBoxRepository();
                storageBoxRepository.loadStorageBox(player.getUniqueId());
            }
            else if(args[0].equalsIgnoreCase("save")) {
                InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
                infinityTowerRepository.saveAllInfinityTower();

                MonsterRepository monsterRepository = new MonsterRepository();
                monsterRepository.saveAllMonster();

                RewardRepository rewardRepository = new RewardRepository();
                rewardRepository.saveAllReward();

                StorageBoxRepository storageBoxRepository = new StorageBoxRepository();
                storageBoxRepository.saveStorageBox(player.getUniqueId());
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
                InfinityTowerProcess infinityTowerProcess = new InfinityTowerProcess(player, InfinityTowerRepository.infinityTowerHashMap.get(infinityTowerService.nameToUUID(args[1])));
                infinityTowerProcess.start();


                player.sendMessage(PluginRepository.pluginConfig.getAllowedCommands().toString());
            }

        }





        return false;
    }
}
