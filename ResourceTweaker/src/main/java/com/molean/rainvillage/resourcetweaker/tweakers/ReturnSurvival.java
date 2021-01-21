package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import com.molean.rainvillage.resourcetweaker.menu.PlayerMenu;
import com.molean.rainvillage.resourcetweaker.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ReturnSurvival implements CommandExecutor, TabCompleter, Listener {
    public ReturnSurvival() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
        Bukkit.getPluginCommand("spawn").setExecutor(this);
        Bukkit.getPluginCommand("spawn").setTabCompleter(this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Material material = event.getMaterial();
        Action action = event.getAction();
        if (!material.equals(Material.CLOCK))
            return;
        if (event.useItemInHand() == Event.Result.DENY) {
            return;
        }
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(ResourceTweaker.class), () -> {
                new PlayerMenu(event.getPlayer()).open();

            });
        }
        event.setCancelled(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            BungeeUtils.switchServer(((Player) sender), "survival");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
