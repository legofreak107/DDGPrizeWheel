package me.legofreak107.ddgprizewheel.commands;

import me.legofreak107.ddgprizewheel.Main;
import me.legofreak107.ddgprizewheel.libs.ItemTagHandler;
import me.legofreak107.ddgprizewheel.methods.DatabaseHandler;
import me.legofreak107.ddgprizewheel.methods.ItemStackCreator;
import me.legofreak107.ddgprizewheel.objects.Database;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RewardCommand implements CommandExecutor {

    /**
     * This is executed when a player uses the reward command.
     *
     * @param commandSender
     * @param command
     * @param s
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        // Check if the command is "reward"
        if (!command.getName().equalsIgnoreCase("reward")) return false;

        // Check if the executor is a player.
        if (!(commandSender instanceof Player)) return false;

        Player p = (Player) commandSender;

        // Check if the player has the "wheel.admin" permissions.
        if (!p.hasPermission("wheel.admin")) {
            commandSender.sendMessage("§cError: §7You do not have the right permissions to do this!");
            return false;
        }

        // Check if the argument length is higher then 0. If not. Return the possible commands.
        if (!(args.length > 0)) {
            commandSender.sendMessage("§7Commands:");
            commandSender.sendMessage("§c/reward add §7| §cAdd the item you are holding to the rewards.");
            commandSender.sendMessage("§c/reward remove <id> §7| §cRemove the id");
            commandSender.sendMessage("§c/reward list §7| §cShow a list of reward id's.");
            commandSender.sendMessage("§c/reward givecoin §7| §cReceive a wheel coin");
            return false;
        }

        if (args[0].equalsIgnoreCase("add")) {
            // Check if the item in the players hand is not null
            if (p.getInventory().getItemInMainHand() != null) {
                // Check if the item in the players hand is not air
                if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    // Insert the item in the database
                    DatabaseHandler.insertReward(p.getInventory().getItemInMainHand());
                    commandSender.sendMessage("§aReward added!");
                } else {
                    commandSender.sendMessage("§cError: §7You are not holding an item!");
                }
            } else {
                commandSender.sendMessage("§cError: §7You are not holding an item!");
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                // Check if the entered number is a valid Integer.
                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (Exception ignored) {
                    commandSender.sendMessage("§cInvalid argument: §7Id is not a valid number");
                    return false;
                }
                if (Main.getInstance().getRewards().containsKey(id)) {
                    // Remove the reward from the database.
                    DatabaseHandler.removeReward(id);
                    commandSender.sendMessage("§aReward removed!");
                } else {
                    commandSender.sendMessage("§cInvalid argument: §7Id does not exist.");
                }
            } else {
                commandSender.sendMessage("§cCommand usage: §7/reward remove <id>");
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            // Display all valid id's from the rewards.
            commandSender.sendMessage("§cReward list:");
            for (Integer id : Main.getInstance().getRewards().keySet()) {
                commandSender.sendMessage("§c  " + id + " §7- §cx" + Main.getInstance().getRewards().get(id).getAmount() + " " + Main.getInstance().getRewards().get(id).getType().name());
            }
        } else if (args[0].equalsIgnoreCase("givecoin")) {
            // Give a wheel coin to the player
            commandSender.sendMessage("§aWheelcoin received!");
            ItemStack wheelCoin = ItemStackCreator.generateItem(Material.IRON_INGOT, 1, 0, "§aWheelcoin");
            wheelCoin = ItemTagHandler.addString(wheelCoin, "nbt.mtcustom", "wheelcoin");
            p.getInventory().addItem(wheelCoin);
        } else {
            // Return the available commands.
            commandSender.sendMessage("§7Commands:");
            commandSender.sendMessage("§c/reward add §7| §cAdd the item you are holding to the rewards.");
            commandSender.sendMessage("§c/reward remove <id> §7| §cRemove the id");
            commandSender.sendMessage("§c/reward list §7| §cShow a list of reward id's.");
            commandSender.sendMessage("§c/reward givecoin §7| §cReceive a wheel coin");
        }
        return false;
    }
}
