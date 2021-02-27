package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import com.molean.rainvillage.resourcetweaker.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DropLimit implements Listener {

    private final Set<Material> blockWhiteList = new HashSet<>();
    private final Set<EntityType> entityWhiteList = new HashSet<>();
    private final Set<Material> itemWhiteList = new HashSet<>();

    public DropLimit() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
        blockWhiteList.add(Material.CHEST);
        blockWhiteList.add(Material.CRAFTING_TABLE);
        blockWhiteList.add(Material.FURNACE);
        blockWhiteList.add(Material.ANVIL);
        blockWhiteList.add(Material.CHIPPED_ANVIL);
        blockWhiteList.add(Material.DAMAGED_ANVIL);
        blockWhiteList.add(Material.ENCHANTING_TABLE);
        blockWhiteList.add(Material.LOOM);
        blockWhiteList.add(Material.CAMPFIRE);
        blockWhiteList.add(Material.STONECUTTER);
        blockWhiteList.add(Material.BARREL);
        blockWhiteList.add(Material.BLAST_FURNACE);
        blockWhiteList.add(Material.SMOKER);
        blockWhiteList.add(Material.FLETCHING_TABLE);
        blockWhiteList.add(Material.CARTOGRAPHY_TABLE);
        blockWhiteList.add(Material.DIRT);
        blockWhiteList.add(Material.SAND);
        blockWhiteList.add(Material.NETHERRACK);
        blockWhiteList.add(Material.COBBLESTONE);
        blockWhiteList.add(Material.GRANITE);
        blockWhiteList.add(Material.RED_SAND);
        blockWhiteList.add(Material.DIORITE);
        blockWhiteList.add(Material.ANDESITE);
        blockWhiteList.add(Material.GRASS_BLOCK);
        blockWhiteList.add(Material.SANDSTONE);
        blockWhiteList.add(Material.COAL_ORE);
        blockWhiteList.add(Material.SOUL_SAND);

        for (Material value : Material.values()) {
            if (value.name().toLowerCase().contains("shulker_box")) {
                blockWhiteList.add(value);
            }
        }

        itemWhiteList.add(Material.SNOWBALL);
        itemWhiteList.add(Material.CLAY_BALL);

        entityWhiteList.add(EntityType.PLAYER);

    }

    private boolean canObtain(String chunkID, ItemStack itemStack) {
        if (itemWhiteList.contains(itemStack.getType())) {
            return true;
        }
        YamlConfiguration config = ConfigUtils.getConfig("DropLimit.yml");
        int time = config.getInt(chunkID + "." + itemStack.getType().name(), 0);
        if (time >= 128) {
            return false;
        }
        time += itemStack.getAmount();
        config.set(chunkID + "." + itemStack.getType().name(), time);
        ConfigUtils.saveConfig("DropLimit.yml");
        return true;
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        if (entityWhiteList.contains(event.getEntityType())) {
            return;
        }
        Chunk chunk = event.getEntity().getChunk();
        String chunkKey = chunk.getX() + "." + chunk.getZ();
        List<ItemStack> items = event.getDrops();
        List<ItemStack> tobeDelete = new ArrayList<>();
        for (ItemStack item : items) {
            if (!canObtain(chunkKey, item)) {
                tobeDelete.add(item);
            }
        }
        items.removeAll(tobeDelete);
    }


    @EventHandler
    public void on(BlockDropItemEvent event) {
        if (blockWhiteList.contains(event.getBlock().getType())) {
            return;
        }
        Chunk chunk = event.getBlock().getChunk();
        String chunkKey = chunk.getX() + "." + chunk.getZ();
        List<Item> items = event.getItems();
        List<Item> tobeDelete = new ArrayList<>();
        for (Item item : items) {
            if (!canObtain(chunkKey, item.getItemStack())) {
                tobeDelete.add(item);
            }
        }
        items.removeAll(tobeDelete);
    }
}
