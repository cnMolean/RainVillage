package com.molean.rainvillage.survivaltweaker.npcshop;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class NPCShopMenu implements Listener {

    private final Player player;
    private final Inventory inventory;
    private final NPCShop npcShop;

    public NPCShopMenu(Player player, NPCShop npcShop) {
        this.player = player;
        this.npcShop = npcShop;
        inventory = Bukkit.createInventory(player, 27, npcShop.getTitle());
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SurvivalTweaker.class));
    }

    public void open() {
        for (int i = 0; i < npcShop.getItemShops().size(); i++) {
            ItemStackSheet itemShop = new ItemStackSheet(npcShop.getItemShops().get(i).getMaterial());
            itemShop.addLore("§d " + npcShop.getItemShops().get(i).getPrice() + " ✿");
            inventory.setItem(i, itemShop.build());
        }
        Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(SurvivalTweaker.class), () -> player.openInventory(inventory));
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() != inventory) {
            return;
        }
        event.setCancelled(true);
        if (!event.getClick().equals(ClickType.LEFT)) {
            return;
        }
        int slot = event.getSlot();
        if (slot < 0) {
            return;
        }
        if (slot >= npcShop.getItemShops().size()) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SurvivalTweaker.class), () -> {
            new ItemShopMenu(player, npcShop, npcShop.getItemShops().get(slot)).open();
        });
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
