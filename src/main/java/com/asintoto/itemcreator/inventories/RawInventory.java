package com.asintoto.itemcreator.inventories;

import com.asintoto.itemcreator.items.serializers.ItemSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class RawInventory implements Cloneable {
    private int size;
    private String title;
    private OfflinePlayer source;
    private InventoryHolder holder;
    private Map<Integer, ItemSerializer<?>> content;

    public RawInventory() {
        this.size = 27;
        this.title = "No Title Provided";
        this.content = new HashMap<>();
    }

    @Override
    public RawInventory clone() {
        try {
            final RawInventory clone = (RawInventory) super.clone();
            clone.content = Map.copyOf(this.content);
            clone.size = this.size;
            clone.title = this.title;
            clone.source = this.source;
            clone.holder = this.holder;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
