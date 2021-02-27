package com.molean.rainvillage.rainvillagetweaker;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class JoinLeftMessage implements Listener {
    private final Set<String> joinSet = new HashSet<>();
    private final Set<String> leftSet = new HashSet<>();

    public JoinLeftMessage() {
        ProxyServer.getInstance().getPluginManager().registerListener(RainVillageTweaker.getPlugin(), this);
        ProxyServer.getInstance().getScheduler().schedule(RainVillageTweaker.getPlugin(), () -> {
            synchronized (joinSet) {
                if (joinSet.size() > 0) {
                    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                        String message;
                        if (player.getLocale().getLanguage().equals("zh")) {
                            message = "§e" + String.join(",", joinSet) + " 加入了游戏.";
                        } else {
                            message = "§e" + String.join(",", joinSet) + " join the game.";
                        }
                        player.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
                    }
                    joinSet.clear();
                }
            }
            synchronized (leftSet) {
                if (leftSet.size() > 0) {
                    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                        String message;
                        if (player.getLocale().getLanguage().equals("zh")) {
                            message = "§e" + String.join(",", leftSet) + " 离开了游戏.";
                        } else {
                            message = "§e" + String.join(",", leftSet) + " left the game.";
                        }
                        player.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
                    }
                    leftSet.clear();
                }
            }

        }, 0, 30, TimeUnit.SECONDS);
    }


    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        if (joinSet.contains(event.getPlayer().getName())) {
            joinSet.remove(event.getPlayer().getName());
        } else {
            leftSet.add(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        String name = event.getPlayer().getName();
        if (!name.matches("[0-9a-zA-z_]{3,16}")) {
            if (event.getPlayer().getLocale().getLanguage().equals("zh")) {
                event.getPlayer().disconnect(new TextComponent("用户名不合法!"));
            } else {
                event.getPlayer().disconnect(new TextComponent("Username is not legal!"));
            }
            return;
        }
        if (leftSet.contains(event.getPlayer().getName())) {
            leftSet.remove(event.getPlayer().getName());
        } else {
            joinSet.add(event.getPlayer().getName());
        }
    }

}
