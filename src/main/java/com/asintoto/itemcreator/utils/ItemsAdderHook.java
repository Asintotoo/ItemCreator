package com.asintoto.itemcreator.utils;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderHook {
    private static boolean checkItemsAdder() {
        return Bukkit.getServer().getPluginManager().getPlugin("ItemsAdder") != null;
    }

    public static ItemStack getItemsAdderItemStack(String namespace_id) {
        if(!checkItemsAdder()) {
            return new ItemStack(Material.STONE);
        }

        if(!CustomStack.isInRegistry(namespace_id)) {
            return new ItemStack(Material.STONE);
        }

        return CustomStack.getInstance(namespace_id).getItemStack();
    }
}
