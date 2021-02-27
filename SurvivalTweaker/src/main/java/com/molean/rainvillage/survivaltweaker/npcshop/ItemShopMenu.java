package com.molean.rainvillage.survivaltweaker.npcshop;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import com.molean.rainvillage.survivaltweaker.utils.EconomyUtils;
import com.molean.rainvillage.survivaltweaker.utils.ItemI18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ItemShopMenu implements Listener {

    private final Player player;
    private final Inventory inventory;
    private final NPCShop npcShop;
    private final ItemShop itemShop;

    public ItemShopMenu(Player player, NPCShop npcShop, ItemShop itemShop) {
        this.player = player;
        this.npcShop = npcShop;
        this.itemShop = itemShop;
        inventory = Bukkit.createInventory(player, 45, npcShop.getTitle() + " " + ItemI18n.get(itemShop.getMaterial()));
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SurvivalTweaker.class));
    }

    public void open() {
        int amount = NPCShopManager.calculateCanSell(player, npcShop, itemShop);
        if (amount == 0) {
            NPCShopManager.sendMessageIfNotEnough(player, npcShop, itemShop, 1);
            Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(SurvivalTweaker.class), (Runnable) player::closeInventory);
            return;
        }
        for (int i = 4 * 9; i < 5 * 9; i++) {
            ItemStackSheet itemStackSheet = new ItemStackSheet(Material.RED_STAINED_GLASS_PANE, "§f返回");
            inventory.setItem(i, itemStackSheet.build());
        }

        int playerPerDayData = NPCShopManager.getPlayerPerDayData(player, npcShop, itemShop);
        int perDayData = NPCShopManager.getPerDayData(npcShop, itemShop);
        int playerTotalData = NPCShopManager.getPlayerTotalData(player, npcShop, itemShop);

        int maxPerDay = itemShop.getMaxPerDay();
        int maxPerPlayer = itemShop.getMaxPerPlayer();
        int maxPerPlayerPerDay = itemShop.getMaxPerPlayerPerDay();

        ItemStackSheet info = new ItemStackSheet(Material.EMERALD_BLOCK, "§f仓库容量");
        info.addLore("§d总仓库: " + playerTotalData + "/" + maxPerPlayer);
        info.addLore("§d今日公共仓库: " + perDayData + "/" + maxPerDay);
        info.addLore("§d今日个人仓库: " + playerPerDayData + "/" + maxPerPlayerPerDay);
        inventory.setItem(40, info.build());


        if (amount >= 1) {
            ItemStackSheet sellAll = new ItemStackSheet(Material.RED_WOOL, "§f卖出全部");
            sellAll.addLore("§dSHIFT+左键");
            inventory.setItem(4, sellAll.build());
            ItemStackSheet sell1 = new ItemStackSheet(itemShop.getMaterial(), "§f卖出1个", 1);
            inventory.setItem(19, sell1.build());
        }
        if (amount >= 4) {
            ItemStackSheet sell4 = new ItemStackSheet(itemShop.getMaterial(), "§f卖出4个", 4);
            inventory.setItem(21, sell4.build());
        }
        if (amount >= 16) {

            ItemStackSheet sell16 = new ItemStackSheet(itemShop.getMaterial(), "§f卖出16个", 16);
            inventory.setItem(23, sell16.build());
        }
        if (amount >= 64) {
            ItemStackSheet sell64 = new ItemStackSheet(itemShop.getMaterial(), "§f卖出64个", 64);
            inventory.setItem(25, sell64.build());
        }


        Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(SurvivalTweaker.class), () -> player.openInventory(inventory));
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() != inventory) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getSlot();
        if (slot < 0) {
            return;
        }
        int max = NPCShopManager.calculateCanSell(player, npcShop, itemShop);
        int has = has(player.getInventory(), itemShop.getMaterial());
        Material material = itemShop.getMaterial();
        if (event.getClick().equals(ClickType.LEFT)) {
            switch (slot) {

                case 19: {
                    sell(player, npcShop, itemShop, 1);
                    break;
                }
                case 21: {
                    sell(player, npcShop, itemShop, 4);
                    break;
                }
                case 23: {
                    sell(player, npcShop, itemShop, 16);
                    break;
                }
                case 25: {
                    sell(player, npcShop, itemShop, 64);
                    break;
                }
                case 36:
                case 37:
                case 38:
                case 39:
                case 41:
                case 42:
                case 43:
                case 44: {
                    Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SurvivalTweaker.class), () -> {
                        new NPCShopMenu(player, npcShop).open();
                    });
                }
            }
        } else if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
            if (slot == 4) {
                sell(player, npcShop, itemShop, Math.min(max, has));
            }
        }

    }

    private static void sell(Player player, NPCShop npcShop, ItemShop itemShop, int amount) {
        if (!check(player, itemShop.getMaterial(), amount)) {
            return;
        }
        int take = take(player.getInventory(), itemShop.getMaterial(), amount);
        int perDayData = NPCShopManager.getPerDayData(npcShop, itemShop);
        int playerTotalData = NPCShopManager.getPlayerTotalData(player, npcShop, itemShop);
        int playerPerDayData = NPCShopManager.getPlayerPerDayData(player, npcShop, itemShop);
        NPCShopManager.setPerDayData(npcShop, itemShop, perDayData + amount);
        NPCShopManager.setPlayerTotalData(player, npcShop, itemShop, playerTotalData + amount);
        NPCShopManager.setPlayerPerDayData(player, npcShop, itemShop, playerPerDayData + amount);

        double money = itemShop.getPrice() * take;
        EconomyUtils.giveMoney(player, money);
        player.sendMessage("§d╰ ( ￣ ω ￣ ｏ ) §f§l这次的报酬一共是[ §c§l" + money + "§f§l ]元 ，请收好！");

        Logger logger = JavaPlugin.getPlugin(SurvivalTweaker.class).getLogger();
        String message = "§7" + player.getName() + " 出售了 " + take + " 个 " +
                ItemI18n.get(itemShop.getMaterial()) + " 获得 " + money;

        logger.info(message);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.isOp()) {
                onlinePlayer.sendMessage(message);
            }
        }

        player.closeInventory();
    }

    private static boolean check(Player player, Material material, int amount) {
        if (has(player.getInventory(), material) < amount) {
            player.sendMessage("§9( ╯ ▔皿▔ ) ╯ §f§l做生意要讲究诚信！你这点数量根本不够啊！");
            player.closeInventory();
            return false;
        }
        return true;
    }

    private static int has(Inventory inventory, Material material) {
        int cur = 0;
        for (ItemStack itemStack : inventory) {
            if (itemStack != null && itemStack.getType().equals(material)) {
                cur += itemStack.getAmount();
            }
        }
        return cur;
    }

    private static int take(Inventory inventory, Material material, int amount) {
        int cur = 0;
        for (ItemStack itemStack : inventory) {
            if (itemStack != null && itemStack.getType().equals(material)) {
                if (cur + itemStack.getAmount() >= amount) {
                    itemStack.setAmount(itemStack.getAmount() - amount + cur);
                    return amount;
                } else {
                    cur += itemStack.getAmount();
                    itemStack.setAmount(0);
                }
            }
        }
        return cur;

    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory() != inventory) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory() != inventory) {
            return;
        }
        event.getHandlers().unregister(this);
    }
}
