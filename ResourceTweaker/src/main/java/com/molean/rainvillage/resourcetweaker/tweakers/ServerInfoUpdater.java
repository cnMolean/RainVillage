package com.molean.rainvillage.resourcetweaker.tweakers;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getScheduler;

public class ServerInfoUpdater implements PluginMessageListener {

    private static String serverName;

    public static String getServerName() {
        return serverName;
    }

    private static final List<String> onlinePlayers = new ArrayList<>();

    public static List<String> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers);
    }

    private static final List<String> servers = new ArrayList<>();

    public static List<String> getServers() {
        return new ArrayList<>(servers);
    }

    public ServerInfoUpdater() {
        Bukkit.getMessenger().registerIncomingPluginChannel(JavaPlugin.getPlugin(ResourceTweaker.class), "BungeeCord", this);
        getScheduler().runTaskTimerAsynchronously(JavaPlugin.getPlugin(ResourceTweaker.class), ServerInfoUpdater::updates, 20, 20);
    }

    public static void updates() {
        updateOnlinePlayers();
        updateServerName();
        updateServers();
    }

    public static void updateOnlinePlayers() {
        @SuppressWarnings("all") ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");
        out.writeUTF("PlayerList");
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player != null)
            player.sendPluginMessage(JavaPlugin.getPlugin(ResourceTweaker.class), "BungeeCord", out.toByteArray());
    }

    public static void updateServerName() {
        @SuppressWarnings("all") ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        out.writeUTF("GetServer");
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player != null)
            player.sendPluginMessage(JavaPlugin.getPlugin(ResourceTweaker.class), "BungeeCord", out.toByteArray());
    }

    public static void updateServers() {
        @SuppressWarnings("all") ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        out.writeUTF("GetServers");
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player != null)
            player.sendPluginMessage(JavaPlugin.getPlugin(ResourceTweaker.class), "BungeeCord", out.toByteArray());
    }

    public static UUID getUUID(String player) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player).getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        @SuppressWarnings("all") ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("PlayerList")) {
            String server = in.readUTF();
            String[] playerList = in.readUTF().split(", ");
            onlinePlayers.clear();
            onlinePlayers.addAll(Arrays.asList(playerList));
        } else if (subChannel.equalsIgnoreCase("GetServer")) {
            serverName = in.readUTF();
        } else if (subChannel.equalsIgnoreCase("GetServers")) {
            String[] serverList = in.readUTF().split(", ");
            servers.clear();
            servers.addAll(Arrays.asList(serverList));
        }
    }
}
