package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import io.papermc.paper.event.player.PlayerTradeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoVillageTrader implements Listener {
    public NoVillageTrader() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
    }

    @EventHandler
    public void on(CreatureSpawnEvent event) {
        if (event.getEntity().getType().equals(EntityType.VILLAGER)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerTradeEvent event) {
        AbstractVillager villager = event.getVillager();
        event.setCancelled(true);
        villager.remove();
    }
}
