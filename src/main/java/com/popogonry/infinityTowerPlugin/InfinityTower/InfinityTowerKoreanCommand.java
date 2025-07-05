package com.popogonry.infinityTowerPlugin.InfinityTower;

import com.popogonry.infinityTowerPlugin.Area.AreaRepository;
import com.popogonry.infinityTowerPlugin.Area.AreaService;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.NameNotFoundException;
import com.popogonry.infinityTowerPlugin.InfinityTower.Exception.UUIDAlreadyExistsException;
import com.popogonry.infinityTowerPlugin.Monster.MonsterRepository;
import com.popogonry.infinityTowerPlugin.Monster.MonsterService;
import com.popogonry.infinityTowerPlugin.PluginRepository;
import com.popogonry.infinityTowerPlugin.Reference;
import com.popogonry.infinityTowerPlugin.Reward.RewardRepository;
import com.popogonry.infinityTowerPlugin.StorageBox.StorageBoxGUI;
import com.popogonry.infinityTowerPlugin.TextDisplayHologram.TextDisplayHologramService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InfinityTowerKoreanCommand implements CommandExecutor {
    AreaService areaService = new AreaService();
    InfinityTowerService infinityTowerService = new InfinityTowerService();
    MonsterService monsterService = new MonsterService();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("보상")) {
                    StorageBoxGUI storageBoxGUI = new StorageBoxGUI();
                    storageBoxGUI.openStorageBoxGUI(((Player) sender).getPlayer(), 1);
                }
            }
        }

        if(!sender.isOp()) {

            sender.sendMessage(Reference.prefix_normal + "===== 무한의탑 =====");
            sender.sendMessage(Reference.prefix_normal + "/무한의탑 보상 : 무한의탑 보상함을 엽니다.");
            return true;
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
                return true;

            }
            else if(args[0].equalsIgnoreCase("세이브")) {
                InfinityTowerRepository infinityTowerRepository = new InfinityTowerRepository();
                infinityTowerRepository.saveAllInfinityTower();

                MonsterRepository monsterRepository = new MonsterRepository();
                monsterRepository.saveAllMonster();

                RewardRepository rewardRepository = new RewardRepository();
                rewardRepository.saveAllReward();
                sender.sendMessage(Reference.prefix_dataSave + "세이브 완료하였습니다.");
                return true;

            }

            else if(args[0].equalsIgnoreCase("목록")) {
                infinityTowerService.printInfinityTowers(sender);

                return true;

            } else if (args[0].equalsIgnoreCase("몬스터목록")) {
                MonsterService.printMonsterList(sender);
                return true;

            } else if (args[0].equalsIgnoreCase("랭킹목록")) {

                TextDisplayHologramService.sendTextDisplayHologramLocationMap(sender);

                return true;
            } else if (args[0].equalsIgnoreCase("몬스터생성")) {
                monsterService.createDefaultMonster();
                sender.sendMessage(Reference.prefix_normal + "기본 몬스터를 생성했습니다.");
                return true;
            }

            if(sender instanceof Player) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("도구")) {
                    player.getInventory().addItem(areaService.getAreaSettingTool());
                    sender.sendMessage(Reference.prefix_normal + "구역 설정 도구가 지급되었습니다.");
                    return true;
                }
            }
        }
        else if(args.length == 2) {

            if(sender instanceof Player) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("구역설정모드")) {
                    AreaService areaService = new AreaService();

                    if(args[1].equalsIgnoreCase("설정")) {
                        areaService.onAreaSettingMode(((Player) sender).getPlayer());
                        sender.sendMessage(Reference.prefix_normal + "구역 설정 모드가 설정 상태입니다.");
                        return true;
                    }
                    else if(args[1].equalsIgnoreCase("해제")) {
                        areaService.offAreaSettingMode(((Player) sender).getPlayer());
                        sender.sendMessage(Reference.prefix_normal + "구역 설정 모드가 해제 상태입니다.");
                        return true;
                    }
                }
                else if(args[0].equalsIgnoreCase("구역설정")) {
                    try {
                        infinityTowerService.updateInfinityTower(args[1], AreaRepository.playerAreaHashMap.getOrDefault(player, null), null);
                        sender.sendMessage(Reference.prefix_normal + "구역 설정 완료되었습니다.");
                    } catch (NameNotFoundException e) {
                        sender.sendMessage(Reference.prefix_error + e.getMessage());
                    }
                    return true;
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
                    return true;
                }
                else if(args[0].equalsIgnoreCase("보상설정")) {
                    if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                        player.sendMessage(Reference.prefix_error + "보상을 손에 들어주세요.");
                        return false;
                    }
                    try {
                        RewardRepository.rewardsHashMap.put(Integer.valueOf(args[1]), new ItemStack(player.getInventory().getItemInMainHand()));
                        RewardRepository.rewardsSet.add(Integer.valueOf(args[1]));
                        player.sendMessage(Reference.prefix_normal + "정상적으로 추가되었습니다.");
                    } catch (Exception e) {
                        player.sendMessage(Reference.prefix_error + "명령어 실행 중 오류가 발생했습니다.");
                        return false;
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
                return true;
            }
            else if(args[0].equalsIgnoreCase("제거")) {
                try {
                    infinityTowerService.deleteInfinityTower(args[1]);
                    sender.sendMessage(Reference.prefix_normal + "제거 완료되었습니다.");
                } catch (NameNotFoundException e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }
                return true;
            }
            else if(args[0].equalsIgnoreCase("활성화")) {
                try {
                    infinityTowerService.onInfinityTower(args[1]);
                    sender.sendMessage(Reference.prefix_normal + "활성화 완료되었습니다.");

                } catch (Exception e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }
                return true;
            }
            else if(args[0].equalsIgnoreCase("비활성화")) {
                try {
                    infinityTowerService.offInfinityTower(args[1]);
                    sender.sendMessage(Reference.prefix_normal + "비활성화 완료되었습니다.");

                } catch (NameNotFoundException e) {
                    sender.sendMessage(Reference.prefix_error + e.getMessage());
                }
                return true;
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
                return true;
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
                return true;

            }
            else if(args[0].equalsIgnoreCase("랭킹제거")) {
                try {
                    TextDisplayHologramService textDisplayHologramService = new TextDisplayHologramService();
                    textDisplayHologramService.removeHologram(args[1]);
                    sender.sendMessage(Reference.prefix_normal + args[1] + " 랭킹표 제거 완료되었습니다.");
                } catch (Exception e) {
                    sender.sendMessage(Reference.prefix_error + "존재하지 않는 랭킹표입니다.");
                }
                return true;
            }
        }
        else if(args.length == 3) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(args[0].equalsIgnoreCase("랭킹생성")) {
                    try {
                        TextDisplayHologramService textDisplayHologramService = new TextDisplayHologramService();
                        textDisplayHologramService.createHologram(args[1], player.getLocation(), args[2]);
                        sender.sendMessage(Reference.prefix_normal + args[1] + " 랭킹표 생성 완료되었습니다.");
                    } catch (Exception e) {
                        sender.sendMessage(Reference.prefix_error + "이미 존재 하는 이름이거나, 타입이 잘못되었습니다.");
                        sender.sendMessage(Reference.prefix_normal + "/무한의탑 랭킹생성 [이름] [daily/weekly/monthly]");
                    }
                    return true;
                }
            }
        }

        sender.sendMessage(ChatColor.WHITE + "§m                                 ");
        sender.sendMessage(ChatColor.GREEN + "    📚 무한의탑 명령어 도움말");
        sender.sendMessage(ChatColor.WHITE + "§m                                 ");

        sender.sendMessage(ChatColor.WHITE + "▶ 일반 명령어");
        sendCommand(sender, "/무한의탑 보상", "무한의탑 보상함을 엽니다.", ChatColor.GOLD);

        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "▶ 무한의 탑 관련");
        sendCommand(sender, "/무한의탑 로드", "플러그인 데이터를 불러옵니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 세이브", "플러그인 데이터를 저장합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 목록", "생성된 무한의탑 목록을 확인합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 생성 [이름]", "새 무한의탑을 생성합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 제거 [이름]", "무한의탑을 삭제합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 활성화 [이름]", "무한의탑을 활성화합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 비활성화 [이름]", "무한의탑을 비활성화합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 강제종료 [이름]", "진행 중인 무한의탑을 강제 종료합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 보상설정 [층수]", "해당 층의 보상을 손에 든 아이템으로 설정합니다.", ChatColor.GOLD);

        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "▶ 몬스터 / 구역 설정");
        sendCommand(sender, "/무한의탑 몬스터목록", "몬스터 목록을 확인합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 몬스터생성", "새로운 몬스터 템플릿을 생성합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 도구", "구역 설정 도구를 지급합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 구역설정모드 설정", "구역 설정 모드를 켭니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 구역설정모드 해제", "구역 설정 모드를 끕니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 구역설정 [이름]", "해당 무한의탑의 구역을 설정합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 스폰설정 [이름]", "현재 위치를 해당 탑의 스폰지점으로 설정합니다.", ChatColor.GOLD);

        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "▶ 기타 기능");
        sendCommand(sender, "/무한의탑 티켓지급 [플레이어]", "입장 티켓을 해당 플레이어에게 지급합니다.", ChatColor.GOLD);

        sender.sendMessage("");
        sender.sendMessage(ChatColor.WHITE + "▶ 랭킹 시스템");
        sendCommand(sender, "/무한의탑 랭킹목록", "생성된 랭킹표 목록을 확인합니다.", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 랭킹생성 [이름] [주기]", "랭킹표를 생성합니다.", ChatColor.GOLD);
        sendCommand(sender, "[주기]", "[daily / weekly / monthly]", ChatColor.GOLD);
        sendCommand(sender, "/무한의탑 랭킹제거 [이름]", "해당 이름의 랭킹표를 제거합니다.", ChatColor.GOLD);

        sender.sendMessage(ChatColor.WHITE + "§m                                 ");




        return false;
    }

    private String padRightWithKoreanAware(String text, int totalWidth) {
        int width = 0;
        for (char c : text.toCharArray()) {
            width += (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                    Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO ||
                    Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) ? 2 : 1;
        }

        int padding = totalWidth - width;
        if (padding <= 0) return text;

        return text + " ".repeat(padding);
    }

    private void sendCommand(CommandSender sender, String command, String description, ChatColor commandColor) {
        String padded = padRightWithKoreanAware(command, 36); // 칸 조정은 상황에 따라 늘려도 됨
        sender.sendMessage(commandColor + " - " + padded + ChatColor.WHITE + ": " + description);
    }
}
