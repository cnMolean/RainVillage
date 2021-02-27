package com.molean.rainvillage.survivaltweaker.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyUtils {

    private static Economy economy = null;

    private static void setupEconomy() {
        if (economy == null) {
            RegisteredServiceProvider<Economy> reg = Bukkit.getServicesManager().getRegistration(Economy.class);
            assert reg != null;
            economy = reg.getProvider();
        }
    }

    public static double getMoney(Player player) {
        setupEconomy();
        return economy.getBalance(player);
    }

    public static boolean hasMoney(Player player, double money) {
        setupEconomy();
        return economy.has(player, money);
    }

    public static boolean takeMoney(Player player, double money) {
        setupEconomy();
        EconomyResponse economyResponse = economy.withdrawPlayer(player, money);
        return economyResponse.type.equals(EconomyResponse.ResponseType.SUCCESS);
    }

    public static boolean giveMoney(Player player, double money) {
        setupEconomy();
        EconomyResponse economyResponse = economy.depositPlayer(player, money);
        return economyResponse.type.equals(EconomyResponse.ResponseType.SUCCESS);
    }

}
