package me.legofreak107.ddgprizewheel.objects;

import me.legofreak107.ddgprizewheel.Main;
import me.legofreak107.ddgprizewheel.libs.ItemTagHandler;
import me.legofreak107.ddgprizewheel.methods.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wheel {

    private List<ItemStack> rewards = new ArrayList<>();
    private Player player;
    private Inventory inv;
    private boolean stop = false;

    /**
     * Constructor for the Wheel object the rewards are filled, inventory is opened and spin is started.
     *
     * @param player the player that is opening the wheel
     */
    public Wheel(Player player) {
        this.player = player;
        fillRewards();
        inv = Bukkit.createInventory(player, 9 * 5, "Beloningen");
        startSpin();
        player.setMetadata("wheel", new FixedMetadataValue(Main.getInstance(), this));
    }

    /**
     * The method to fill the reward list with new rewards.
     * This is done randomly. 8 rewards are picked from the list with all rewards.
     * There will be no duplicate rewards.
     */
    private void fillRewards() {

        List<ItemStack> tempRewards = new ArrayList<>(Main.getInstance().getRewards().values());

        // Shuffle the list of temp rewards to randomize it.
        Collections.shuffle(tempRewards);

        // Loop up to 8 times to add 8 rewards.
        for (int i = 0; i < 8 && i < tempRewards.size(); i++) {
            rewards.add(tempRewards.get(i));
        }

        // Check if the list is 8, if not add random extra rewards.
        while (rewards.size() < 8) {
            Collections.shuffle(tempRewards);
            rewards.add(tempRewards.get(0));
        }
    }

    /**
     * Fill the inventory with the default items.
     * Initiate the spin animation for the player.
     * At the end the player receives the drawn item.
     */
    private void startSpin() {
        // Loop through the whole inventory and fill it with glass panes (light blue)
        for (int i = 0; i < 9 * 5; i++) {
            inv.setItem(i, ItemStackCreator.generateItem(Material.STAINED_GLASS_PANE, 1, 3, "§a"));
        }

        // Loop through the middle row of the inventory and fill it with glass panes (red)
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, ItemStackCreator.generateItem(Material.STAINED_GLASS_PANE, 1, 14, "§a"));
        }

        // Set slot 4 to the "selector" slot so the player knows what item will be drawn
        inv.setItem(4, ItemStackCreator.generateItem(Material.STAINED_GLASS_PANE, 1, 2, "§a\\/"));

        // Start the scheduler
        new BukkitRunnable() {

            // The current state the animation is in.
            int timer = 0;

            // The state the timer has to reach to proceed.
            int waitTimer = 0;

            // The delay for the waitTimer to increase.
            int delay = 0;

            @Override
            public void run() {
                // Check if the player left before the animation was ended.
                if (!stop) {

                    // Check if the current timer is higher then the waitTimer,
                    // if this is not the case, increase timer.
                    if (timer > waitTimer) {

                        // If the delay variable is higher then 2, increase the waitTimer to slow down the animation
                        // and reset the delay to 0. If this is not the case, increase the delay.
                        if (delay > 2) {
                            waitTimer++;
                            delay = 0;
                        } else {
                            delay++;
                        }

                        // Reset the timer.
                        timer = 0;

                        // Play the next animation frame.
                        next();

                        // If the waitTimer reaches 10, give the player the reward and cancel the animation scheduler.
                        if (waitTimer == 10) {
                            giveReward();
                            this.cancel();
                        }

                        // Play a nice sound effect to improve player experience
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                    } else {
                        timer++;
                    }
                    player.openInventory(inv);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }

    /**
     * Finally give the reward to the player
     */
    private void giveReward() {
        // Play a nice sound effect to improve player experience
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1, 1);

        // Retrieve the winning item from the inventory.
        ItemStack toGive = inv.getItem(13).clone();

        // Fill the inventory with glass panes.
        for (int i = 0; i < 9 * 5; i++) {
            inv.setItem(i, ItemStackCreator.generateItem(Material.STAINED_GLASS_PANE, 1, 3, "§a"));
        }
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, ItemStackCreator.generateItem(Material.STAINED_GLASS_PANE, 1, 14, "§a"));
        }
        inv.setItem(21, ItemStackCreator.generateItem(Material.STAINED_GLASS_PANE, 1, 3, "§a"));
        inv.setItem(23, ItemStackCreator.generateItem(Material.STAINED_GLASS_PANE, 1, 3, "§a"));

        // Set the winning item in the most center slot.
        inv.setItem(22, toGive);

        // Give the player the winning item.
        player.getInventory().addItem(toGive);

        // Delay the closing of the inventory so the player is able to see his newly retrieved item.
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();

                // Remove the wheel metadata. So the player is able to interact with inventories again.
                if (player.hasMetadata("wheel")) player.removeMetadata("wheel", Main.getInstance());
            }
        }.runTaskLater(Main.getInstance(), 50L);
    }


    /**
     * Set the next animation frame in the inventory.
     * Rotate the reward list by one and re-assign the items to the correct inventory slots.
     */
    private void next() {
        // You can use Collections.shuffle to get the random effect described in the assignment.
        // But I liked the "real" spinning effect more.
        Collections.rotate(rewards, -1);

        inv.setItem(12, rewards.get(0));
        inv.setItem(21, rewards.get(1));
        inv.setItem(30, rewards.get(2));
        inv.setItem(31, rewards.get(3));
        inv.setItem(32, rewards.get(4));
        inv.setItem(23, rewards.get(5));
        inv.setItem(14, rewards.get(6));
        inv.setItem(13, rewards.get(7));
    }

    /**
     * Stop the item drawing if needed. When a player left for example.
     * Stop the animation scheduler and give the wheelcoin back to the player.
     */
    public void stop() {
        stop = true;
        ItemStack wheelCoin = ItemStackCreator.generateItem(Material.IRON_INGOT, 1, 0, "§aWheelcoin");
        wheelCoin = ItemTagHandler.addString(wheelCoin, "nbt.mtcustom", "wheelcoin");
        player.getInventory().addItem(wheelCoin);
    }
}
