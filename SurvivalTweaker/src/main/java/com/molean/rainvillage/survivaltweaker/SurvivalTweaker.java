package com.molean.rainvillage.survivaltweaker;

import com.molean.rainvillage.survivaltweaker.tweakers.*;
import com.molean.rainvillage.survivaltweaker.utils.ConfigUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalTweaker extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        ConfigUtils.setupConfig(this);
        ConfigUtils.configOuput("config.yml");
        new ServerInfoUpdater();
        new ChatTweaker();
        new ResourceTeleporter();
        new HungerKeeper();
        new EnchantmentBook();
        new MoreItem();
        new ClockMenu();
        new LoreEditor();
        new SpawnTeleporter();
        new RemoveJoinLeftMessage();
        new DisableSpawner();
    }

}
