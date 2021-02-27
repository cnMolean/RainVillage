package com.molean.rainvillage.logindispatcher;

import org.bukkit.plugin.java.JavaPlugin;

public final class LoginDispatcher extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new SurvivalCommand();
        new DispatcherCommand();
        new ChatTweaker();
        new RemoveJoinLeftMessage();
    }

}
