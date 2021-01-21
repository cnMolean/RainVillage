package com.molean.rainvillage.survivaltweaker.tweakers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class LoreEditor implements CommandExecutor, TabCompleter {
    public LoreEditor() {
        Bukkit.getPluginCommand("lore").setExecutor(this);
        Bukkit.getPluginCommand("lore").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand.getType().equals(Material.AIR)) {
            return true;
        }
        List<String> lore = itemInMainHand.getLore();
        lore = lore != null ? lore : new ArrayList<>();
        if (args.length == 0) {
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "append": {
                if (args.length >= 2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i].replace('&','§'));
                        if (i != args.length - 1) {
                            stringBuilder.append(" ");
                        }
                    }
                    lore.add(stringBuilder.toString());
                }
                break;
            }
            case "insert": {
                if (args.length >= 3) {
                    int pos = Integer.parseInt(args[1]);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        if (i != args.length - 1) {
                            stringBuilder.append(" ");
                        }
                    }
                    lore.add(pos, stringBuilder.toString());
                }
                break;
            }
            case "remove": {
                if (args.length >= 2) {
                    int pos = Integer.parseInt(args[1]);
                    lore.remove(pos);
                }
                break;
            }
            case "clear": {
                lore.clear();
                break;
            }
            case "name": {
                ItemMeta itemMeta = itemInMainHand.getItemMeta();
                if (args.length >= 2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i].replace('&','§'));
                        if (i != args.length - 1) {
                            stringBuilder.append(" ");
                        }
                    }
                    itemMeta.setDisplayName(stringBuilder.toString());
                }

                itemInMainHand.setItemMeta(itemMeta);
            }

        }
        itemInMainHand.setLore(lore);
        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player player = (Player) sender;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand.getType().equals(Material.AIR)) {
            return null;
        }
        List<String> lore = itemInMainHand.getLore();
        lore = lore != null ? lore : new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        switch (args.length) {
            case 1: {
                strings.add("add");
                strings.add("insert");
                strings.add("remove");
                strings.add("clear");
                strings.add("name");
            }
            case 2: {
                switch (args[0].toLowerCase()) {
                    case "insert": {
                        for (int i = 0; i < lore.size() + 1; i++) {
                            strings.add(i + "");
                        }
                        break;
                    }
                    case "remove":{
                        for (int i = 0; i < lore.size(); i++) {
                            strings.add(i + "");
                        }
                        break;
                    }
                }
            }
        }
        return strings;
    }
}
