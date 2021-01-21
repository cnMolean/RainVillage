package com.molean.rainvillage.survivaltweaker.tweakers;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveJoinLeftMessage implements Listener {
    public RemoveJoinLeftMessage() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SurvivalTweaker.class));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }
}
