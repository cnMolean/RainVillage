package com.molean.rainvillage.rainvillagetweaker.tweakers;

import com.molean.rainvillage.rainvillagetweaker.RainVillageTweaker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinLeftMessage implements Listener {
    public JoinLeftMessage() {
        ProxyServer instance = ProxyServer.getInstance();
        instance.getPluginManager().registerListener(RainVillageTweaker.getPlugin(), this);
    }

    @EventHandler
    public void on(PostLoginEvent event) {
        ProxyServer instance = ProxyServer.getInstance();
        for (ProxiedPlayer player : instance.getPlayers()) {
            String message = "§e" + event.getPlayer().getName() + " 加入了游戏.";
            player.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
        }
    }

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        ProxyServer instance = ProxyServer.getInstance();
        for (ProxiedPlayer player : instance.getPlayers()) {
            String message = "§e" + event.getPlayer().getName() + " 加入了游戏.";
            player.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
        }
    }
}
