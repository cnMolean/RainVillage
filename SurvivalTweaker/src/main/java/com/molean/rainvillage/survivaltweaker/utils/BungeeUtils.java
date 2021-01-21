package com.molean.rainvillage.survivaltweaker.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//@SuppressWarnings("all")
public class BungeeUtils {

    public static void switchServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(server);
        player.sendPluginMessage(JavaPlugin.getPlugin(SurvivalTweaker.class), "BungeeCord", out.toByteArray());
    }

    public static void sendMessage(String player, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(player);
        out.writeUTF(message);
        Player first = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if(first==null){
            return;
        }
        first.sendPluginMessage(JavaPlugin.getPlugin(SurvivalTweaker.class), "BungeeCord", out.toByteArray());
    }

    public static void randomJoinRequest(Player player, int radius) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("resource");
            out.writeUTF("randomJoin");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeUTF(player.getName());
            msgout.writeInt(radius);
            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
            player.sendPluginMessage(JavaPlugin.getPlugin(SurvivalTweaker.class), "BungeeCord", out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
