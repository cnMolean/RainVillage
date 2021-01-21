package com.molean.rainvillage.survivaltweaker.tweakers;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import com.molean.rainvillage.survivaltweaker.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatTweaker implements Listener {
    public ChatTweaker() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SurvivalTweaker.class));
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        String message = "<" + event.getPlayer().getName() + "> " + event.getMessage();

        for (String onlinePlayer : ServerInfoUpdater.getOnlinePlayers()) {
            BungeeUtils.sendMessage(onlinePlayer, message);
        }
    }
}
