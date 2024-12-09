package com.asintoto.itemcreator.items.providers;

import com.asintoto.itemcreator.items.serializers.ItemSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemProvider extends ItemSerializer<ItemProvider> {

    public ItemProvider(final Material material) {
        this.material(material);
    }

    public ItemProvider(final ItemStack item) {
        this.item(item);
    }

}
