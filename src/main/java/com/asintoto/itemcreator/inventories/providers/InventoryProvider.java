package com.asintoto.itemcreator.inventories.providers;

import com.asintoto.itemcreator.InventoryCreator;
import com.asintoto.itemcreator.inventories.RawInventory;
import com.asintoto.itemcreator.items.providers.ItemProvider;
import com.asintoto.itemcreator.items.serializers.ItemSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


@Getter
@Accessors(fluent = true) @SuppressWarnings("all")
@NoArgsConstructor
public class InventoryProvider implements Cloneable{

    private RawInventory inventory = new RawInventory();

    public InventoryProvider setItem(int slot, ItemSerializer<?> item) {
        inventory.content().put(slot, item);
        return this;
    }

    public InventoryProvider setItem(int slot, ItemStack item) {
        return setItem(slot, new ItemProvider(item));
    }

    public InventoryProvider setItem(int slot, Material material) {
        return setItem(slot, new ItemProvider(material));
    }

    public InventoryCreator build() {
        return new InventoryCreator(this);
    }

    @Override
    public InventoryProvider clone() {
        try {
            final InventoryProvider clone = (InventoryProvider) super.clone();
            clone.inventory = this.inventory.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public InventoryProvider size(int size) {
        inventory.size(size);
        return this;
    }

    public InventoryProvider title(String title) {
        inventory.title(title);
        return this;
    }

    public InventoryProvider holder(InventoryHolder holder) {
        inventory.holder(holder);
        return this;
    }

    public InventoryProvider source(OfflinePlayer source) {
        inventory.source(source);
        return this;
    }

}
