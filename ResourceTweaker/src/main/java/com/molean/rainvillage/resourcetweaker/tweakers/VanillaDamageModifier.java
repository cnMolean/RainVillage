package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VanillaDamageModifier implements Listener {

    public VanillaDamageModifier() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(ResourceTweaker.class));
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER)) {
            return;
        }
        Player player = (Player) event.getEntity();

        switch (event.getCause()) {
            case CONTACT:
            case SUFFOCATION:
            case FALL:
            case FIRE:
            case FIRE_TICK:
            case MELTING:
            case LAVA:
            case DROWNING:
            case VOID:
            case LIGHTNING:
            case STARVATION:
            case POISON:
            case MAGIC:
            case WITHER:
            case FALLING_BLOCK:
            case THORNS:
            case DRAGON_BREATH:
            case FLY_INTO_WALL:
            case HOT_FLOOR:
            case CRAMMING:
            case DRYOUT:
                AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                assert attribute != null;
                attribute.getValue();
                event.setDamage(attribute.getValue() / 20 * event.getDamage());
        }
    }
}
