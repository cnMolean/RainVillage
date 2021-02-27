package com.molean.rainvillage.rainvillagetweaker;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.SocketAddress;

public class UniversalChat implements Listener {
    public UniversalChat() {
        ProxyServer.getInstance().getPluginManager().registerListener(RainVillageTweaker.getPlugin(), this);
    }

    @EventHandler
    public void onMessageReceived(PluginMessageEvent event) {
        byte[] data = event.getData();
        String tag = event.getTag();
        if (tag.equalsIgnoreCase("BungeeCord")) {
            try {
                ByteArrayDataInput in = ByteStreams.newDataInput(data);
                String type = in.readUTF();
                if (type.equalsIgnoreCase("Forward")) {
                    String server = in.readUTF();
                    String subchannel = in.readUTF();
                    if (subchannel.equalsIgnoreCase("chat")) {
                        short len = in.readShort();
                        byte[] msgbytes = new byte[len];
                        in.readFully(msgbytes);
                        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                        String p = msgin.readUTF();
                        String m = msgin.readUTF();
                        String channel = msgin.readUTF();
                        String prefix = "";
                        if (channel.startsWith("server")) {
                            prefix = "[空岛]";
                        }
                        if (channel.equalsIgnoreCase("survival")) {
                            prefix = "[源乡]";
                        }
                        if (channel.equalsIgnoreCase("resource")) {
                            prefix = "[霖谷]";
                        }
                        if (channel.equalsIgnoreCase("login")) {
                            prefix = "[车站]";
                        }
                        if (channel.equalsIgnoreCase("resource2")) {
                            prefix = "[岚谷]";
                        }
                        String finalMessage = prefix + "<" + p + "> " + m;

                        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                            player.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(finalMessage));
                        }

                        ProxyServer.getInstance().getLogger().info(finalMessage);
                        if (Robot.getBot() != null) {
                            ProxyServer.getInstance().getScheduler().runAsync(RainVillageTweaker.getPlugin(), () -> {
                                Robot.getBot().getGroup(865260261L).sendMessage(finalMessage);
                            });
                        }
                    }
                }
            } catch (Throwable exception) {
                exception.printStackTrace();
            }
        }
    }
}
