package com.molean.rainvillage.rainvillagetweaker;

import com.molean.rainvillage.rainvillagetweaker.tweakers.JoinLeftMessage;
import net.md_5.bungee.api.plugin.Plugin;

public final class RainVillageTweaker extends Plugin {

    private static Plugin plugin;


    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        new JoinLeftMessage();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
