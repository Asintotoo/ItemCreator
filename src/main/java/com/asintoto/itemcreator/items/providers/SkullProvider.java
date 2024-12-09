package com.asintoto.itemcreator.items.providers;

import com.asintoto.itemcreator.items.serializers.ItemSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SkullProvider extends ItemSerializer<SkullProvider> {

    public SkullProvider() {
        this.item(new ItemStack(Material.PLAYER_HEAD));
    }

    public SkullProvider texture(final String texture) {
        this.item.texture(texture);
        return this;
    }

    public SkullProvider owner(final String owner) {
        this.item.owner(owner);
        return this;
    }

}
