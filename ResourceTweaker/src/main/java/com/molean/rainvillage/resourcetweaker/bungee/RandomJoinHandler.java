package com.molean.rainvillage.resourcetweaker.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomJoinHandler implements PluginMessageListener, Listener {

    private static final Map<String, Integer> requests = new HashMap<>();

    public RandomJoinHandler() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
        Bukkit.getMessenger().registerIncomingPluginChannel(JavaPlugin.getPlugin(ResourceTweaker.class),
                "BungeeCord", this);
    }


    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (requests.containsKey(event.getPlayer().getName())) {
            randomTeleport(event.getPlayer(), requests.get(event.getPlayer().getName()));
            requests.remove(event.getPlayer().getName());
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        @SuppressWarnings("all") ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("randomJoin")) {
            try {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);
                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));

                String source = msgin.readUTF();
                int radius = msgin.readInt();
                Player sourcePlayer = Bukkit.getPlayer(source);
                if (sourcePlayer != null) {
                    randomTeleport(player, radius);
                } else {
                    requests.put(source, radius);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void randomTeleport(Player player, int radius) {
        Random random = new Random();
        int x = random.nextInt(radius);
        int sqrt = (int) Math.sqrt(radius * radius - x * x);
        int z = random.nextBoolean() ? sqrt : -sqrt;
        int y = -1;

        Location location = player.getLocation();

        World world = Bukkit.getWorld("world");

        for (int i = 255; i > 0; i--) {
            Block blockAt = world.getBlockAt(x, i, z);
            if (!blockAt.getType().equals(Material.AIR)) {
                y = i + 2;
                break;
            }
        }

        if (y == -1) {
            return;
        }


        player.teleport(new Location(Bukkit.getWorld("world"), x, y, z, location.getYaw(), location.getPitch()));
    }
}
