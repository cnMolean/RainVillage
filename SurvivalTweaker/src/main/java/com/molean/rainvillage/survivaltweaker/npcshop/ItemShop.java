package com.molean.rainvillage.survivaltweaker.npcshop;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ItemShop {
    private Material material;
    private double price;
    private int maxPerDay;
    private int maxPerPlayerPerDay;
    private int maxPerPlayer;

    public ItemShop(Material material, ConfigurationSection section) {
        this.material = material;
        price = section.getDouble("price");
        maxPerDay = section.getInt("maxPerDay");
        maxPerPlayerPerDay = section.getInt("maxPerPlayerPerDay");
        maxPerPlayer = section.getInt("maxPerPlayer");
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxPerDay() {
        return maxPerDay;
    }

    public void setMaxPerDay(int maxPerDay) {
        this.maxPerDay = maxPerDay;
    }

    public int getMaxPerPlayerPerDay() {
        return maxPerPlayerPerDay;
    }

    public void setMaxPerPlayerPerDay(int maxPerPlayerPerDay) {
        this.maxPerPlayerPerDay = maxPerPlayerPerDay;
    }

    public int getMaxPerPlayer() {
        return maxPerPlayer;
    }

    public void setMaxPerPlayer(int maxPerPlayer) {
        this.maxPerPlayer = maxPerPlayer;
    }

}
