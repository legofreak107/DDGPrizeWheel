package me.legofreak107.ddgprizewheel;

import me.legofreak107.ddgprizewheel.commands.RewardCommand;
import me.legofreak107.ddgprizewheel.commands.WheelCommand;
import me.legofreak107.ddgprizewheel.events.InventoryClick;
import me.legofreak107.ddgprizewheel.events.PlayerQuit;
import me.legofreak107.ddgprizewheel.methods.DatabaseHandler;
import me.legofreak107.ddgprizewheel.objects.Database;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    private HashMap<Integer, ItemStack> rewards = new HashMap<>();
    private static Main instance;
    private Database database;

    /**
     * We initialize the database, and we fetch all the possible rewards.
     * We print the loaded reward count into the console for debug purposes.
     * Then we register the reward and wheel command and we register both events.
     */
    @Override
    public void onEnable() {
        instance = this;
        database = new Database();
        DatabaseHandler.initializeDatabase();
        DatabaseHandler.fetchRewards();
        System.out.println("Loaded " + rewards.size() + " rewards.");
        getCommand("reward").setExecutor(new RewardCommand());
        getCommand("wheel").setExecutor(new WheelCommand());
        new InventoryClick();
        new PlayerQuit();
    }

    /**
     * We close the HikariCP database connections.
     */
    @Override
    public void onDisable() {
        getDatabase().getDataSource().close();
    }

    /**
     * Return the main instance
     * @return JavaPlugin of this project
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Return the id's and linked ItemStacks of the rewards
     * @return rewards HashMap (id and ItemStack)
     */
    public HashMap<Integer, ItemStack> getRewards() {
        return rewards;
    }

    /**
     * Return the database class
     * @return Database object class
     */
    public Database getDatabase() {
        return database;
    }
}