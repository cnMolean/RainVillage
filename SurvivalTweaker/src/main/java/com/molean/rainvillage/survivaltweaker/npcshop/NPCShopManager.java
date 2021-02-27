package com.molean.rainvillage.survivaltweaker.npcshop;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import com.molean.rainvillage.survivaltweaker.utils.ConfigUtils;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NPCShopManager implements Listener {
    private static final List<NPCShop> npcShops = new ArrayList<>();

    public NPCShopManager() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SurvivalTweaker.class));
        try {
            reloadShop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void on(NPCRightClickEvent event) {
        for (NPCShop npcShop : npcShops) {
            if (npcShop.getId() == event.getNPC().getId()) {
                Player clicker = event.getClicker();
                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SurvivalTweaker.class), () -> {
                    new NPCShopMenu(clicker, npcShop).open();
                });
                break;
            }
        }
    }

    public static int getPerDayData(NPCShop npcShop, ItemShop itemShop) {

        YamlConfiguration config = ConfigUtils.getConfig("NPCShopData.yml");

        String datePath = npcShop.getName() + "." + itemShop.getMaterial().name() + ".PurchaseAmount";

        String recordPath = npcShop.getName() + "." + itemShop.getMaterial().name() + ".RecordDate";

        String recordDate = config.getString(datePath);
        if (recordDate == null) {
            return 0;
        } else {
            LocalDate parse = LocalDate.parse(recordDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            if (!parse.isEqual(now)) {
                return 0;
            }
        }
        return config.getInt(recordPath);
    }


    public static int getPlayerTotalData(Player player, NPCShop npcShop, ItemShop itemShop) {
        YamlConfiguration config = ConfigUtils.getConfig("NPCShopData.yml");
        String recordPath = npcShop.getName() + "." + itemShop.getMaterial().name() +
                ".PlayerData." + player.getName() + "." + "SellAmount";
        return config.getInt(recordPath, 0);
    }

    public static int getPlayerPerDayData(Player player, NPCShop npcShop, ItemShop itemShop) {
        YamlConfiguration config = ConfigUtils.getConfig("NPCShopData.yml");

        String datePath = npcShop.getName() + "." + itemShop.getMaterial().name() +
                ".PlayerData." + player.getName() + ".RecordDate";

        String recordPath = npcShop.getName() + "." + itemShop.getMaterial().name() +
                ".PlayerData." + player.getName() + ".SellAmountInDay";


        String recordDate = config.getString(datePath);
        if (recordDate == null) {
            return 0;
        } else {
            LocalDate parse = LocalDate.parse(recordDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            if (!parse.isEqual(now)) {
                return 0;
            }
        }
        return config.getInt(recordPath);
    }


    public static void setPlayerPerDayData(Player player, NPCShop npcShop, ItemShop itemShop, int amount) {
        YamlConfiguration config = ConfigUtils.getConfig("NPCShopData.yml");

        String datePath = npcShop.getName() + "." + itemShop.getMaterial().name() +
                ".PlayerData." + player.getName() + ".RecordDate";

        String recordPath = npcShop.getName() + "." + itemShop.getMaterial().name() +
                ".PlayerData." + player.getName() + ".SellAmountInDay";

        config.set(datePath, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        config.set(recordPath, amount);
        ConfigUtils.saveConfig("NPCShopData.yml");
    }

    public static void setPlayerTotalData(Player player, NPCShop npcShop, ItemShop itemShop, int amount) {
        YamlConfiguration config = ConfigUtils.getConfig("NPCShopData.yml");
        String recordPath = npcShop.getName() + "." + itemShop.getMaterial().name() +
                ".PlayerData." + player.getName() + "." + "SellAmount";
        config.set(recordPath, amount);
        ConfigUtils.saveConfig("NPCShopData.yml");
    }

    public static void setPerDayData(NPCShop npcShop, ItemShop itemShop, int amount) {
        YamlConfiguration config = ConfigUtils.getConfig("NPCShopData.yml");

        String datePath = npcShop.getName() + "." + itemShop.getMaterial().name() + ".PurchaseAmount";

        String recordPath = npcShop.getName() + "." + itemShop.getMaterial().name() + ".RecordDate";

        config.set(datePath, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        config.set(recordPath, amount);
        ConfigUtils.saveConfig("NPCShopData.yml");
    }

    public static int calculateCanSell(Player player, NPCShop npcShop, ItemShop itemShop) {
        int maxPerPlayer = itemShop.getMaxPerPlayer();
        int maxPerDay = itemShop.getMaxPerDay();
        int maxPerPlayerPerDay = itemShop.getMaxPerPlayerPerDay();

        int perDayData = getPerDayData(npcShop, itemShop);
        int playerPerDayData = getPlayerPerDayData(player, npcShop, itemShop);
        int playerTotalData = getPlayerTotalData(player, npcShop, itemShop);

        int tempMin = Math.min(maxPerDay - perDayData, maxPerPlayer - playerTotalData);
        int result = Math.min(maxPerPlayerPerDay - playerPerDayData, tempMin);
        return Math.max(result, 0);
    }

    public static boolean sendMessageIfNotEnough(Player player, NPCShop npcShop, ItemShop itemShop, int amount) {
        int maxPerPlayer = itemShop.getMaxPerPlayer();
        int maxPerDay = itemShop.getMaxPerDay();
        int maxPerPlayerPerDay = itemShop.getMaxPerPlayerPerDay();

        int perDayData = getPerDayData(npcShop, itemShop);
        int playerPerDayData = getPlayerPerDayData(player, npcShop, itemShop);
        int playerTotalData = getPlayerTotalData(player, npcShop, itemShop);

        if (maxPerDay - perDayData < amount) {
            player.sendMessage("§6 （ ノへ￣、）§f§l 今日已收购到足够多的货物了。明天，记得早点来哦！");
            return true;
        }
        if (maxPerPlayerPerDay - playerPerDayData < amount) {
            player.sendMessage("§6（ ＞ 人 ＜ ；）§f§l 今日你的仓库已满仓，请明日再来吧。");
            return true;
        }
        if (maxPerPlayer - playerTotalData <= amount) {
            player.sendMessage("§6(ノω<。)ノ))☆.。§f§l 恭喜你已完成所有库存需求，请收下这份礼物吧！");
            return true;
        }
        return false;
    }


    public void reloadShop() {
        npcShops.clear();
        YamlConfiguration config = ConfigUtils.getConfig("NPCShop.yml");
        for (String key : config.getKeys(false)) {
            npcShops.add(new NPCShop(key, Objects.requireNonNull(config.getConfigurationSection(key))));
        }
    }
}
