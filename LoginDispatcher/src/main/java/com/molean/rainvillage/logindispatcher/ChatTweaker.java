package com.molean.rainvillage.logindispatcher;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChatTweaker implements Listener {
    public ChatTweaker() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(LoginDispatcher.class));
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        universalChat(event.getPlayer(),event.getMessage());
    }
    public static void universalChat(Player player, String message) {
        try {
            @SuppressWarnings("all") ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("BungeeCord");
            out.writeUTF("chat");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeUTF(player.getName());
            msgout.writeUTF(message);
            msgout.writeUTF("login");
            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
            player.sendPluginMessage(JavaPlugin.getPlugin(LoginDispatcher.class), "BungeeCord", out.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
