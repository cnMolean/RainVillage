package com.molean.rainvillage.survivaltweaker.tweakers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoreItem implements CommandExecutor {
    public MoreItem() {
        Bukkit.getPluginCommand("more").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            ItemStack itemInMainHand = ((Player) sender).getInventory().getItemInMainHand();
            int maxStackSize = itemInMainHand.getType().getMaxStackSize();
            itemInMainHand.setAmount(maxStackSize);
        }
        return true;
    }
}
