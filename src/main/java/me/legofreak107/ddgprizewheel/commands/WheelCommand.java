package me.legofreak107.ddgprizewheel.commands;

import me.legofreak107.ddgprizewheel.libs.ItemTagHandler;
import me.legofreak107.ddgprizewheel.objects.Wheel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WheelCommand implements CommandExecutor {

    /**
     * This is executed when a player uses the wheel command.
     *
     * @param commandSender
     * @param command
     * @param s
     * @param strings
     * @return
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Check if the command is wheel.
        if (!command.getName().equalsIgnoreCase("wheel")) return false;

        // Check if the executor is a player.
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        ItemStack wheelCoin = null;
        boolean hasItem = false;

        // Loop through the items in the inventory and check the nbtTag for each item. If the item has the wheelcoin tag.
        // Mark it as a wheelcoin and break the for loop.
        for (ItemStack item : player.getInventory().getContents()) {
            if (ItemTagHandler.getString(item, "nbt.mtcustom").equalsIgnoreCase("wheelcoin")) {
                hasItem = true;
                wheelCoin = item;
                break;
            }
        }

        // If the WheelCoin was found, remove one and spin the wheel.
        if (hasItem) {
            ItemStack wheelCoin1 = wheelCoin.clone();
            wheelCoin1.setAmount(1);
            player.getInventory().removeItem(wheelCoin1);
            new Wheel(player);
        } else {
            commandSender.sendMessage("§eJij hebt op dit moment geen §cWheelcoin §ein bezit. Jij kan dit commando niet uitvoeren.");
        }

        return false;
    }
}
