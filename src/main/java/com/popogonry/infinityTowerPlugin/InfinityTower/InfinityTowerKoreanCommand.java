package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.AreaRepository;
import com.popogonry.infinityTowerPlugin.Area.AreaService;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.NameNotFoundException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.UUIDAlreadyExistsException;
import com.popogonry.infinityTowerPlugin.Monster.MonsterRepository;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Reference;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfinityTowerKoreanCommand implements CommandExecutor {
    AreaService areaService = new AreaService();
    InfinityTowerService infinityTowerService = new InfinityTowerService();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if(!sender.isOp()) {
            sender.sendMessage(Reference.prefix_error + "op 전용 명령어입니다.");
            return false;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("로드")) {
                PluginRepository pluginRepository = new PluginRepository();
                pluginRepository.loadPluginDataConfig();

                InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
                infinityTowerRepository.loadAllInfinityTower();

                RewardRepository rewardRepository = new RewardRepository();
                rewardRepository.loadAllReward();

                MonsterRepository monsterRepository = new MonsterRepository();
                monsterRepository.loadAllMonster();

                sender.sendMessage(Reference.prefix_dataLoad + "로드 완료하였습니다.");
            }
            else if(args[0].equalsIgnoreCase("세이브")) {
                InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
                infinityTowerRepository.saveAllInfinityTower();

                MonsterRepository monsterRepository = new MonsterRepository();
                monsterRepository.saveAllMonster();

                RewardRepository rewardRepository = new RewardRepository();
                rewardRepository.saveAllReward();
                sender.sendMessage(Reference.prefix_dataSave + "세이브 완료하였습니다.");

            }

            else if(args[0].equalsIgnoreCase("목록")) {
                infinityTowerService.printInfinityTowers(sender);
            }

            if(sender instanceof Player) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("도구")) {
                    player.getInventory().addItem(areaService.getAreaSettingTool());
                    sender.sendMessage(Reference.prefix_normal + "구역 설정 도구가 지급되었습니다.");
                }
            }
        }
        else if(args.length == 2) {

            if(sender instanceof Player) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("구역")) {
                    AreaService areaService = new AreaService();

                    if(args[1].equalsIgnoreCase("설정")) {
                        areaService.onAreaSettingMode(((Player) sender).getPlayer());
                        sender.sendMessage(Reference.prefix_normal + "구역 설정 모드가 설정 상태입니다.");

                    }
                    else if(args[1].equalsIgnoreCase("해제")) {
                        areaService.offAreaSettingMode(((Player) sender).getPlayer());
                        sender.sendMessage(Reference.prefix_normal + "구역 설정 모드가 해제 상태입니다.");

                    }

                }
                else if(args[0].equalsIgnoreCase("구역설정")) {
                    try {
                        infinityTowerService.updateInfinityTower(args[1], AreaRepository.playerAreaHashMap.getOrDefault(player, null), null);
                        sender.sendMessage(Reference.prefix_normal + "구역 설정 완료되었습니다.");
                    } catch (NameNotFoundException e) {
                        sender.sendMessage(Reference.prefix_error + e.getMessage());
                    }
                }
                else if(args[0].equalsIgnoreCase("스폰설정")) {
                    double[] location = new double[3];
                    location[0] = player.getLocation().getX();
                    location[1] = player.getLocation().getY();
                    location[2] = player.getLocation().getZ();

                    try {
                        infinityTowerService.updateInfinityTower(args[1], null, location);
                        sender.sendMessage(Reference.prefix_normal + "스폰 설정 완료되었습니다.");
                    } catch (NameNotFoundException e) {
                        sender.sendMessage(Reference.prefix_error + e.getMessage());
                    }

                }
            }

            if(args[0].equalsIgnoreCase("생성")) {
                try {
                    infinityTowerService.createInfinityTower(args[1], null, null);
                    sender.sendMessage(Reference.prefix_normal + "생성 완료되었습니다.");
                } catch (UUIDAlreadyExistsException e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("삭제")) {
                try {
                    infinityTowerService.deleteInfinityTower(args[1]);
                    sender.sendMessage(Reference.prefix_normal + "삭제 완료되었습니다.");
                } catch (NameNotFoundException e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("활성화")) {
                try {
                    infinityTowerService.onInfinityTower(args[1]);
                    sender.sendMessage(Reference.prefix_normal + "활성화 완료되었습니다.");

                } catch (Exception e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("비활성화")) {
                try {
                    infinityTowerService.offInfinityTower(args[1]);
                    sender.sendMessage(Reference.prefix_normal + "비활성화 완료되었습니다.");

                } catch (NameNotFoundException e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }
            }
            else if(args[0].equalsIgnoreCase("강제종료")) {
                boolean process = false;
                try {
                    process = infinityTowerService.killProcess(infinityTowerService.nameToUUID(args[1]));
                    if(!process) {
                        sender.sendMessage(Reference.prefix_error + "작동 중인 무한의 탑이 아닙니다.");
                    }
                    else {
                        sender.sendMessage(Reference.prefix_normal + "강제종료 완료되었습니다.");
                    }
                } catch (Exception e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }

            }
            else if(args[0].equalsIgnoreCase("티켓지급")) {
                Player player = Bukkit.getPlayer(args[1]);
                if(player != null) {
                    player.getInventory().addItem(infinityTowerService.getInfinityTowerTicket());
                    sender.sendMessage(Reference.prefix_normal + "지급 완료되었습니다.");

                }
                else {
                    sender.sendMessage(Reference.prefix_error + "존재 하지 않는 플레이어입니다.");
                }
            }
        }

        return false;
    }
}
