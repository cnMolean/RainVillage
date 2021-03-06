package com.molean.rainvillage.survivaltweaker.tweakers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SpawnTeleporter implements CommandExecutor, TabCompleter {
    public SpawnTeleporter() {
        Objects.requireNonNull(Bukkit.getPluginCommand("spawn")).setTabCompleter(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("spawn")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Location location = new Location(Bukkit.getWorld("world"), 1465.5, 66, -1658.5);
            ((Player) sender).teleport(location);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
