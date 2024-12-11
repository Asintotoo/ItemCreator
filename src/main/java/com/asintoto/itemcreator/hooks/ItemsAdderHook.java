package com.asintoto.itemcreator.hooks;

import com.asintoto.itemcreator.utils.PluginChecker;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderHook {
    private static boolean checkItemsAdder() {
        return PluginChecker.pluginExists("ItemsAdder");
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
