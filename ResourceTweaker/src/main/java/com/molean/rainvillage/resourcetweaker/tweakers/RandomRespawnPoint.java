package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.bungee.RandomJoinHandler;
import org.bukkit.Statistic;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RandomRespawnPoint implements Listener {
    public void on(PlayerJoinEvent event) {
        int statistic = event.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE);
        if (statistic == 0) {
            RandomJoinHandler.randomTeleport(event.getPlayer(), 500);
        }
    }
}
