package com.molean.rainvillage.survivaltweaker.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.UUID;

public class SkullUtils {
    private static Properties properties;

    public static ItemStack fromOffline(String player) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayerIfCached(player));
        skullMeta.setDisplayName("§e" + player);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    public static ItemStack fromTexture(String texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack fromLibrary(String name) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(SkullUtils.class.getClassLoader().getResourceAsStream("skulls.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fromTexture(properties.getProperty(name));
    }

}
