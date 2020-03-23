package me.legofreak107.ddgprizewheel.methods;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackCreator {

    /**
     * Generate an itemstack based on given variables
     * @param mat Material of the item
     * @param amount Amount of the item
     * @param data Data (durability) of the item
     * @param name Name of the item
     * @return Newly generated ItemStack
     */
    public static ItemStack generateItem(Material mat, int amount, int data, String name) {
        ItemStack item = new ItemStack(mat, amount, (short) data);
        ItemMeta itemm = item.getItemMeta();
        itemm.setDisplayName(name);
        item.setItemMeta(itemm);
        return item;
    }

}
