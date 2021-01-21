package com.molean.rainvillage.survivaltweaker.npcshop;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class FavoriteMenu implements Listener {

    private final Player player;
    private final Inventory inventory;

    public FavoriteMenu(Player player) {
        this.player = player;
        inventory = Bukkit.createInventory(player, 9, );
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin());
    }

    public void open() {
        for (int i = 0; i < 9; i++) {
            ItemStackSheet itemStackSheet = new ItemStackSheet(Material.GRAY_STAINED_GLASS_PANE, " ");
            inventory.setItem(i, itemStackSheet.build());
        }
        ItemStackSheet visit = new ItemStackSheet(Material.REDSTONE_TORCH, I18n.getMessage("menu.favorite.visit", player));
        inventory.setItem(0, visit.build());

        Bukkit.getScheduler().runTask(IsletopiaTweakers.getPlugin(), () -> player.openInventory(inventory));
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

        switch (slot) {
            case 0: {
                ItemStack item = inventory.getItem(slot);
                assert item != null;
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(I18n.getMessage("menu.wait", player));
                item.setItemMeta(itemMeta);
                Bukkit.getScheduler().runTaskAsynchronously(IsletopiaTweakers.getPlugin(), () -> new FavoriteVisitMenu(player).open());
                break;
            }
        }
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
