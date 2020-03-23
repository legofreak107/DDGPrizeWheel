package me.legofreak107.ddgprizewheel.libs;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class ItemTagHandler {
    private static NBTTagCompound getTag(org.bukkit.inventory.ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack itemNms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag;
        if (itemNms.hasTag()) tag = itemNms.getTag();
        else tag = new NBTTagCompound();
        return tag;
    }
    private static org.bukkit.inventory.ItemStack setTag(org.bukkit.inventory.ItemStack item, NBTTagCompound tag) {
        net.minecraft.server.v1_12_R1.ItemStack itemNms = CraftItemStack.asNMSCopy(item);
        itemNms.setTag(tag);
        return CraftItemStack.asBukkitCopy(itemNms);
    }
    public static org.bukkit.inventory.ItemStack addString(org.bukkit.inventory.ItemStack item, String name, String value) {
        NBTTagCompound tag = ItemTagHandler.getTag(item);
        tag.setString(name, value);
        return ItemTagHandler.setTag(item, tag);
    }
    public static boolean hasString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = ItemTagHandler.getTag(item);
        return tag.hasKey(name);
    }
    public static String getString(org.bukkit.inventory.ItemStack item, String name) {
        NBTTagCompound tag = ItemTagHandler.getTag(item);
        return tag.getString(name);
    }
    public static org.bukkit.inventory.ItemStack removeString(org.bukkit.inventory.ItemStack itemStack, String name) {
        NBTTagCompound tag = ItemTagHandler.getTag(itemStack);
        NBTTagCompound newTag = new NBTTagCompound();
        tag.c().stream().filter(name::equals).forEach(string -> {
            newTag.set(string, tag.get(string));
            System.out.println("Remove string: ");
            System.out.println(string);
            System.out.println(tag.get(string));
            System.out.println("End remove string");
        });
        return setTag(itemStack, newTag);
    }
}