package me.legofreak107.ddgprizewheel.events;

import me.legofreak107.ddgprizewheel.Main;
import me.legofreak107.ddgprizewheel.objects.Wheel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    /**
     * Register the event.
     */
    public PlayerQuit() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    /**
     * Check if the player has the wheel metadata. If so. Stop the wheel animation and return the WheelCoin.
     * @param e
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().hasMetadata("wheel")) {
            Wheel wheel = (Wheel)e.getPlayer().getMetadata("wheel").get(0).value();
            wheel.stop();
        }
    }
}
