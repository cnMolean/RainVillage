package com.molean.rainvillage.survivaltweaker.tweakers;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ClockMenu implements CommandExecutor, Listener {
    public ClockMenu() {
        Objects.requireNonNull(Bukkit.getPluginCommand("menu")).setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SurvivalTweaker.class));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length != 0) {
                if (args[0].equals("get")) {
                    if (((Player) sender).getInventory().contains(Material.CLOCK)) {
                        return true;
                    }
                    ItemStack itemStack = new ItemStack(Material.CLOCK);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName("主菜单");
                    itemStack.setItemMeta(itemMeta);
                    ((Player) sender).getInventory().addItem(itemStack);
                }

            } else {
//                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SurvivalTweaker.class),
//                        () -> new MainMenu((Player) sender).open());
            }
        }


        return true;
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
        if (event.getPlayer().isSneaking()) {
            if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                event.getPlayer().performCommand("plot visit " + event.getPlayer().getName());
            }
        }
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            event.getPlayer().performCommand("menu");
        }
        event.setCancelled(true);
    }
}
