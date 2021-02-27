package com.molean.rainvillage.resourcetweaker;

import com.molean.rainvillage.resourcetweaker.bungee.RandomJoinHandler;
import com.molean.rainvillage.resourcetweaker.tweakers.*;
import com.molean.rainvillage.resourcetweaker.utils.ConfigUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class ResourceTweaker extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new ServerInfoUpdater();
        ConfigUtils.setupConfig(this);


        if (!ServerInfoUpdater.getServerName().equalsIgnoreCase("resource1")) {
            new MobStrengthen();
        }
        new VanillaDamageModifier();
        new HungerKeeper();
        new MineLimit();
        new ChatTweaker();
        new ReturnSurvival();
        new VanillaDamageModifier();
        new RandomJoinHandler();
        new RemoveJoinLeftMessage();
        new VanillaDamageModifier();
        new RandomRespawnPoint();
        new CancelRaid();
        new DropLimit();
        new FishingLimit();
        new NoVillageTrader();
    }
}
