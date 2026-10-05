package com.choespacedout.itemPickupToggle;

import com.choespacedout.itemPickupToggle.commands.TogglePickup;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Core extends JavaPlugin implements Listener {

    File playerConfigs;
    PlayerConfigsCache playerConfigsCache;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        getDataFolder().mkdir();

        playerConfigs = new File(getDataFolder(),"playerConfigs.yml");

        if (!playerConfigs.exists()) {
            try {
                playerConfigs.createNewFile();
            } catch (IOException exception) {
                System.out.println("Could not load config file! TpaCommand could not finish loading!");
                return;
            }
        }
        playerConfigsCache  = new PlayerConfigsCache(playerConfigs);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(TogglePickup.createCommand("togglePickup", playerConfigs, playerConfigsCache),"Manage the current world's warps");

        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Player player)) return;

        PlayerConfig playerConfig = playerConfigsCache.getPlayerConfig(player.getUniqueId());
        e.setCancelled(!playerConfig.getItemPickupToggle());
    }
}
