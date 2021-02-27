package com.molean.rainvillage.survivaltweaker;

import com.molean.rainvillage.survivaltweaker.npcshop.NPCShopManager;
import com.molean.rainvillage.survivaltweaker.tweakers.*;
import com.molean.rainvillage.survivaltweaker.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalTweaker extends JavaPlugin {

    public void getEconomy() {

    }

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        ConfigUtils.setupConfig(this);
        ConfigUtils.configOuput("config.yml");
        ConfigUtils.configOuput("NPCShop.yml");
        new ServerInfoUpdater();
        new NPCShopManager();
        new ChatTweaker();
        new ResourceTeleporter();
        new Resource2Teleporter();
        new HungerKeeper();
        new EnchantmentBook();
        new MoreItem();
        new ClockMenu();
        new LoreEditor();
        new SpawnTeleporter();
        new RemoveJoinLeftMessage();
        new DisableSpawner();
        new FishingLimit();
    }

    @Override
    public void onDisable() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.closeInventory();
        }
    }
}
