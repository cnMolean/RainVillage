package com.molean.rainvillage.survivaltweaker.tweakers;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import com.molean.rainvillage.survivaltweaker.utils.ItemI18n;
import com.molean.rainvillage.survivaltweaker.utils.NMSTagUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class EnchantmentBook implements Listener, CommandExecutor, TabCompleter {
    public EnchantmentBook() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SurvivalTweaker.class));
        Objects.requireNonNull(Bukkit.getPluginCommand("book")).setTabCompleter(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("book")).setExecutor(this);
    }

    public static ItemStack getEnchantmentBook(Enchantment enchantment, int minLevel, int maxLevel,
                                               int plusLevel, int chance, int broken, List<Material> materials) {

        ItemStack itemStack = new ItemStack(Material.BOOK);
        itemStack.addUnsafeEnchantment(enchantment, plusLevel);
        itemStack = NMSTagUtils.append(itemStack, "isEnchantmentBook", "true");
        itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-Enchantment", enchantment.getKey().getKey());
        itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-MaxLevel", maxLevel);
        itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-MinLevel", minLevel);
        itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-PlusLevel", plusLevel);
        itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-MaterialCount", materials.size());
        itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-SuccessChance", chance);
        itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-BrokenChance", broken);
        for (int i = 0; i < materials.size(); i++) {
            itemStack = NMSTagUtils.append(itemStack, "EnchantmentBook-Material-" + i, materials.get(i).name());
        }

        assert itemStack != null;

        ArrayList<String> lores = new ArrayList<>();
        lores.add("§e适用等级：" + minLevel + "-" + maxLevel);
        lores.add("§e成功概率：" + String.format("%02d.%02d%%", chance / 100, chance % 100));
        lores.add("§e破损概率：" + String.format("%02d.%02d%%", broken / 100, broken % 100));
        lores.add("§e适用物品：");
        for (Material material : materials) {
            lores.add("§b  " + ItemI18n.get(material));
        }
        itemStack.setLore(lores);
        return itemStack;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER)
            return;
        if (event.getClick() != ClickType.RIGHT)
            return;
        if (event.getAction() != InventoryAction.SWAP_WITH_CURSOR)
            return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;
        if (event.getCursor() == null || event.getCursor().getType() != Material.BOOK)
            return;
        if (event.getCurrentItem().getAmount() != 1)
            return;
        if (event.getCursor().getAmount() != 1)
            return;
        if (!(event.getWhoClicked() instanceof Player))
            return;

        ItemStack target = event.getCurrentItem();
        ItemStack book = event.getCursor();
        Player player = (Player) event.getWhoClicked();

        String isBook = NMSTagUtils.getString(book, "isEnchantmentBook");
        if (!"true".equals(isBook))
            return;
        String enchantmentString = NMSTagUtils.getString(book, "EnchantmentBook-Enchantment");
        Integer maxLevel = NMSTagUtils.getInt(book, "EnchantmentBook-MaxLevel");
        Integer minLevel = NMSTagUtils.getInt(book, "EnchantmentBook-MinLevel");
        Integer plusLevel = NMSTagUtils.getInt(book, "EnchantmentBook-PlusLevel");
        Integer materialCount = NMSTagUtils.getInt(book, "EnchantmentBook-MaterialCount");
        Integer successChance = NMSTagUtils.getInt(book, "EnchantmentBook-SuccessChance");
        Integer brokenChance = NMSTagUtils.getInt(book, "EnchantmentBook-BrokenChance");

        assert enchantmentString != null;
        assert maxLevel != null;
        assert minLevel != null;
        assert plusLevel != null;
        assert materialCount != null;
        assert successChance != null;
        assert brokenChance != null;

        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentString));

        assert enchantment != null;

        List<Material> materials = new ArrayList<>();

        for (int i = 0; i < materialCount; i++) {
            String string = NMSTagUtils.getString(book, "EnchantmentBook-Material-" + i);
            Material material = Material.valueOf(string);
            materials.add(material);
        }

        if (!materials.contains(target.getType())) {
            player.sendMessage("type not match");
            return;
        }
        Map<Enchantment, Integer> enchantments = target.getEnchantments();
        Integer level = enchantments.getOrDefault(enchantment, 0);

        if (level < minLevel) {
            //level too low
            player.sendMessage("level too low");
            return;
        }

        if (level >= maxLevel) {
            player.sendMessage("level too high");
            return;
        }


        book.setAmount(book.getAmount() - 1);
        event.setCancelled(true);
        if (new Random().nextInt(10000) < successChance) {
            target.addUnsafeEnchantment(enchantment, level + plusLevel);
            player.sendMessage("success");
            Location location = player.getLocation();
            location.getWorld().playSound(location, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        } else {
            if (new Random().nextInt(10000) < brokenChance) {
                player.sendMessage("broken");
                event.setCurrentItem(null);
                Location location = player.getLocation();
                location.getWorld().playSound(location, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            } else {
                player.sendMessage("failed");
                Location location = player.getLocation();
                location.getWorld().playSound(location, Sound.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
            }
        }


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //enchantmentbook DAMAGE_ALL 1 0 1000 1000 1000 MATERIAL
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length < 7) {
            sender.sendMessage("参数不足");
            return true;
        }
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[0]));
        int plusLevel = Integer.parseInt(args[1]);
        int minLevel = Integer.parseInt(args[2]);
        int maxLevel = Integer.parseInt(args[3]);
        int successChange = Integer.parseInt(args[4]);
        int brokenChance = Integer.parseInt(args[5]);
        List<Material> materials = new ArrayList<>();
        for (int i = 6; i < args.length; i++) {
            materials.add(Material.valueOf(args[i]));
        }
        ItemStack book = getEnchantmentBook(enchantment, minLevel, maxLevel, plusLevel, successChange, brokenChance, materials);
        ((Player) sender).getInventory().addItem(book);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        switch (args.length) {
            case 1: {
                for (Enchantment value : Enchantment.values()) {
                    if (value.getKey().getKey().toLowerCase().startsWith(args[0].toLowerCase())) {
                        strings.add(value.getKey().getKey());
                    }
                }
                break;
            }
            case 2: {
                strings.add("plusLevel");
                break;
            }
            case 3: {
                strings.add("minLevel");
                break;
            }
            case 4: {
                strings.add("maxLevel");
                break;
            }
            case 5: {
                strings.add("successChance");
                break;
            }
            case 6: {
                strings.add("brokenChance");
                break;
            }
            default: {
                for (Material value : Material.values()) {
                    if (value.name().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                        strings.add(value.name());
                    }
                }
            }
        }
        return strings;
    }
}
