package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import com.molean.rainvillage.resourcetweaker.utils.BungeeUtils;
import com.molean.rainvillage.resourcetweaker.utils.ConfigUtils;
import com.molean.rainvillage.resourcetweaker.utils.ItemI18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineLimit implements Listener {
    private static final Map<Material, Integer> limits = new HashMap<>();

    public MineLimit() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
        limits.put(Material.COAL_ORE, 200);
        limits.put(Material.IRON_ORE, 100);
        limits.put(Material.DIAMOND_ORE, 10);
        limits.put(Material.GOLD_ORE, 100);
        limits.put(Material.EMERALD_ORE, 10);
        limits.put(Material.LAPIS_ORE, 100);
        limits.put(Material.NETHER_GOLD_ORE, 100);
        limits.put(Material.NETHER_QUARTZ_ORE, 100);
        limits.put(Material.REDSTONE_ORE, 100);
        limits.put(Material.ANCIENT_DEBRIS, 1);
    }


    @EventHandler
    public void on(BlockExplodeEvent event) {
        Block block = event.getBlock();
        if (limits.containsKey(block.getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!limits.containsKey(block.getType())) {
            return;
        }

        YamlConfiguration config = ConfigUtils.getConfig("MineLimit.yml");
        String recordTime = config.getString(player.getName() + ".RecordTime");
        if (recordTime == null) {
            String nowString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            config.set(player.getName() + ".RecordTime", nowString);
            config.set(player.getName() + ".Record", null);
        } else {
            LocalDate parse = LocalDate.parse(recordTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            if (!parse.isEqual(now)) {
                config.set(player.getName() + ".RecordTime", now);
                config.set(player.getName() + ".Record", null);
            }
        }

        int time = config.getInt(player.getName() + ".Record." + block.getType().name(), 0);

//        if (time >= limits.get(block.getType())) {
//            event.setCancelled(true);
//            player.sendMessage("§c此类矿物挖掘数量已上限！");
//        } else {
//            config.set(player.getName() + ".Record." + block.getType().name(), time + 1);
//        }
//        ConfigUtils.saveConfig("MineLimit.yml");

        time++;
        config.set(player.getName() + ".Record." + block.getType().name(), time);
        ConfigUtils.saveConfig("MineLimit.yml");

        if(limits.containsKey(block.getType())&&time%limits.get(block.getType())==0){
            String message = "§7[§6矿工快报§7] 恭喜 " + player.getName() + " 今日已挖掘到 『" + time + "』 个" + ItemI18n.get(block.getType());
            List<String> onlinePlayers = ServerInfoUpdater.getOnlinePlayers();
            for (String onlinePlayer : onlinePlayers) {
                BungeeUtils.sendMessage(onlinePlayer, message);
            }
        }


    }

}
