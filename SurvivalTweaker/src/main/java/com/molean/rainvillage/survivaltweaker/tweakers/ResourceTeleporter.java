package com.molean.rainvillage.survivaltweaker.tweakers;

import com.molean.rainvillage.survivaltweaker.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ResourceTeleporter implements CommandExecutor, TabCompleter {
    public ResourceTeleporter() {
        Objects.requireNonNull(Bukkit.getPluginCommand("resource")).setExecutor(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("resource")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        int radius;
        if (args.length != 0) {
            try {
                radius = Integer.parseInt(args[0]);
                BungeeUtils.randomJoinRequest((Player) sender, radius);
            } catch (NumberFormatException ignored) {
            }

        }

        BungeeUtils.switchServer((Player) sender, "resource");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return List.of("radius");
        }
        return List.of();
    }
}
