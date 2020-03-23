package me.legofreak107.ddgprizewheel.methods;

import me.legofreak107.ddgprizewheel.Main;
import me.legofreak107.ddgprizewheel.libs.ItemConverter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public class DatabaseHandler {

    /**
     * Create the rewards table if it does not exist.
     * This is done async so the main server thread does not delay.
     */
    public static void initializeDatabase() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection con = Main.getInstance().getDatabase().getConnection()) {
                    try (PreparedStatement stmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS ddg.rewards ( id INT NOT NULL AUTO_INCREMENT , itemstack TEXT NOT NULL , PRIMARY KEY (id));")) {
                        stmt.execute();
                    }
                } catch (
                        SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    /**
     * Fetch all the rewards from the database and store them in the plugin.
     * This is done async so the main server thread does not delay.
     */
    public static void fetchRewards() {
        System.out.println("Fetch started!");
        Main.getInstance().getRewards().clear();
        new BukkitRunnable() {
            long now = System.currentTimeMillis();

            @Override
            public void run() {
                try (Connection con = Main.getInstance().getDatabase().getConnection()) {
                    try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM ddg.rewards")) {
                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                Main.getInstance().getRewards().put(rs.getInt("id"), ItemConverter.toItem(rs.getString("itemstack")));
                            }
                            long took = System.currentTimeMillis() - now;
                            System.out.println("Fetch done! Took " + took + "MS");
                        }
                    }
                } catch (
                        SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    /**
     * Remove the reward with the given id from the database.
     * This is done async so the main server thread does not delay.
     */
    public static void removeReward(int id) {
        System.out.println("Remove started!");
        Main.getInstance().getRewards().remove(id);
        new BukkitRunnable() {
            long now = System.currentTimeMillis();

            @Override
            public void run() {
                try (Connection con = Main.getInstance().getDatabase().getConnection()) {
                    try (PreparedStatement stmt = con.prepareStatement("DELETE FROM ddg.rewards WHERE ddg.rewards.id = " + id)) {
                        stmt.execute();
                        long took = System.currentTimeMillis() - now;
                        System.out.println("Remove done! Took " + took + "MS");
                    }
                } catch (
                        SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    /**
     * Insert the given reward into the database.
     * This is done async so the main server thread does not delay.
     */
    public static void insertReward(ItemStack reward) {
        System.out.println("Insert started!");
        new BukkitRunnable() {
            long now = System.currentTimeMillis();

            @Override
            public void run() {
                try (Connection con = Main.getInstance().getDatabase().getConnection()) {
                    String itemString = ItemConverter.fromItem(reward);
                    try (PreparedStatement stmt = con.prepareStatement("INSERT INTO ddg.rewards (id, itemstack) VALUES (NULL, '" + itemString + "');")) {
                        stmt.execute();
                        long took = System.currentTimeMillis() - now;
                        System.out.println("Inserting done! Took " + took + "MS");
                    }
                    try (PreparedStatement stmt = con.prepareStatement("SELECT id FROM ddg.rewards WHERE ddg.rewards.itemstack = '" + itemString + "';")) {
                        try (ResultSet rs = stmt.executeQuery()) {
                            rs.next();
                            int id = rs.getInt("id");
                            Main.getInstance().getRewards().put(id, reward);
                        }
                    }
                } catch (
                        SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

}
