package com.molean.rainvillage.resourcetweaker.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeUtils {
    public static void switchServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(server);
        player.sendPluginMessage(JavaPlugin.getPlugin(ResourceTweaker.class), "BungeeCord", out.toByteArray());
    }

    public static void sendMessage(String player, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(player);
        out.writeUTF(message);
        Player first = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (first == null) {
            return;
        }
        first.sendPluginMessage(JavaPlugin.getPlugin(ResourceTweaker.class), "BungeeCord", out.toByteArray());
    }
}
