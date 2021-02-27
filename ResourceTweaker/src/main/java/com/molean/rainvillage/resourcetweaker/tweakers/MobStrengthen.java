package com.molean.rainvillage.resourcetweaker.tweakers;

import com.molean.rainvillage.resourcetweaker.ResourceTweaker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.projectiles.ProjectileSource;

public class MobStrengthen implements Listener {
    public MobStrengthen() {
        Bukkit.getPluginManager().registerEvents(this, ResourceTweaker.getPlugin(ResourceTweaker.class));
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Arrow)) {
            return;
        }
        ProjectileSource shooter = ((Arrow) damager).getShooter();
        if (!(shooter instanceof Skeleton)) {
            return;
        }
        Location location = ((Skeleton) shooter).getLocation();
        double r = Math.sqrt(location.getX() * location.getX() + location.getZ() * location.getZ());
        int level = (int) ((r - 250.0) / 100.0);

        event.setDamage((level * 0.1 + 1) * event.getDamage());
    }

    @EventHandler
    public void on(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Monster)) {
            return;
        }
        Monster monster = (Monster) entity;

        switch (event.getEntity().getEntitySpawnReason()) {
            case BEEHIVE:
            case CUSTOM:
                return;
        }

        Location location = entity.getLocation();
        double r = Math.sqrt(location.getX() * location.getX() + location.getZ() * location.getZ());

        int level = (int) ((r - 250.0) / 100.0);

        AttributeInstance health = monster.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance damage = monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

        assert health != null;
        assert damage != null;

        health.setBaseValue((level * level * 0.02 + 1) * health.getDefaultValue());

        monster.setHealth(health.getBaseValue());

        damage.setBaseValue((level * 0.1 + 1) * damage.getDefaultValue());
    }


}
