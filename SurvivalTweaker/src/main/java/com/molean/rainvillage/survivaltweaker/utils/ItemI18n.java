package com.molean.rainvillage.survivaltweaker.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ItemI18n {
    private static YamlConfiguration configuration = null;

    public static String get(Material material) {
        if (configuration == null) {
            try {
                InputStream inputStream = ItemI18n.class.getClassLoader().getResourceAsStream("itemi18n.yml");
                assert inputStream != null;
                byte[] bytes = inputStream.readAllBytes();
                configuration = new YamlConfiguration();
                configuration.loadFromString(new String(bytes, StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String key = "itemi18n." + material.name().toUpperCase();
        if (configuration.contains(key)) {
            return configuration.getString(key);
        } else {
            return material.name();
        }

    }
}
