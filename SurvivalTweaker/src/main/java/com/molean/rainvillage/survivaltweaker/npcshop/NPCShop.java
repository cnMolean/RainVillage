package com.molean.rainvillage.survivaltweaker.npcshop;

import com.molean.rainvillage.survivaltweaker.SurvivalTweaker;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NPCShop {
    private String name;
    private int id;
    private String title;
    private List<ItemShop> itemShops;


    public NPCShop(String name, ConfigurationSection section) {
        this.name = name;
        id = section.getInt("npc");
        title = section.getString("title");
        itemShops = new ArrayList<>();
        ConfigurationSection itemsSection = section.getConfigurationSection("items");
        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection item = itemsSection.getConfigurationSection(key);
            Material material = Material.getMaterial(key);
            if (material == null) {
                Logger logger = JavaPlugin.getPlugin(SurvivalTweaker.class).getLogger();
                logger.warning(key + " is not a valid material");
            }
            assert item != null;
            itemShops.add(new ItemShop(material, item));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemShop> getItemShops() {
        return itemShops;
    }

    public void setItemShops(List<ItemShop> itemShops) {
        this.itemShops = itemShops;
    }
}
