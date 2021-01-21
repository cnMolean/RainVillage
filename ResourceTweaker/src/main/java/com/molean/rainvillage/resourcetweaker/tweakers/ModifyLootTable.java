package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ModifyLootTable implements Listener {
    private final List<EntityType> list = new ArrayList<>();

    public ModifyLootTable() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
        list.add(EntityType.ZOMBIFIED_PIGLIN);
        list.add(EntityType.IRON_GOLEM);
    }

    @EventHandler
    public void on(EntityDropItemEvent event) {
        if (list.contains(event.getEntityType())) {
            event.setCancelled(true);
        }
    }


}
