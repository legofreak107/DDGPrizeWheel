package me.legofreak107.ddgprizewheel.events;

import me.legofreak107.ddgprizewheel.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    /**
     * Register the event.
     */
    public InventoryClick() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    /**
     * Check if the player has the wheel metadata. If so, cancel all inventory clicks.
     * @param e
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (e.getWhoClicked().hasMetadata("wheel")) {
                e.setCancelled(true);
            }
        }
    }
}
