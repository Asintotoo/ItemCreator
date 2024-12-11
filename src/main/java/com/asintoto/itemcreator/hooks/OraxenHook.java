package com.asintoto.itemcreator.hooks;

import com.asintoto.itemcreator.utils.PluginChecker;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class OraxenHook {
    private static boolean checkOraxen() {
        return PluginChecker.pluginExists("Oraxen");
    }

    public static ItemStack getOraxenItemStack(String namespace_id) {
        if(!PluginChecker.pluginExists("Oraxen")) {
            return new ItemStack(Material.STONE);
        }

        if(!OraxenItems.exists(namespace_id)) {
            return new ItemStack(Material.STONE);

        }

        return OraxenItems.getItemById(namespace_id).build();
    }
}
