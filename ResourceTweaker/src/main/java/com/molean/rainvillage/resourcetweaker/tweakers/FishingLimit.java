package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import com.molean.rainvillage.resourcetweaker.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FishingLimit implements Listener {
    public FishingLimit() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
    }

    @EventHandler
    public void on(PlayerFishEvent event) {
        if (event.getCaught() == null) {
            return;
        }
        String chunkKey = event.getCaught().getChunk().getX() + "." + event.getCaught().getChunk().getZ();

        YamlConfiguration config = ConfigUtils.getConfig("FishingLimit.yml");
        String recordTime = config.getString(chunkKey + ".RecordTime");
        if (recordTime == null) {
            String nowString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            config.set(chunkKey + ".RecordTime", nowString);
            config.set(chunkKey + ".Record", null);
        } else {
            LocalDate parse = LocalDate.parse(recordTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            if (!parse.isEqual(now)) {
                config.set(chunkKey + ".RecordTime", now);
                config.set(chunkKey + ".Record", null);
            }
        }

        int time = config.getInt(chunkKey + ".Record", 0);

        if (time >= 64) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§3||ヽ（。・∀・）ノミ|Ю §3§l这片水域已经没有鱼了，换个地方试试吧！");
        } else {
            config.set(chunkKey + ".Record", time + 1);
        }
        ConfigUtils.saveConfig("FishingLimit.yml");

    }
}
