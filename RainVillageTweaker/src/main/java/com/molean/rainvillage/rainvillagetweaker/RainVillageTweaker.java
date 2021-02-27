package com.molean.rainvillage.rainvillagetweaker;

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
        new SkinSync();
        new UniversalChat();
        new Robot();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
