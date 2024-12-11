package com.asintoto.itemcreator;

import com.asintoto.itemcreator.inventories.RawInventory;
import com.asintoto.itemcreator.inventories.providers.InventoryProvider;
import com.asintoto.itemcreator.items.serializers.ItemSerializer;
import com.asintoto.itemcreator.utils.StringFormatter;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

@AllArgsConstructor
public class InventoryCreator implements Cloneable{

    private InventoryProvider inventoryProvider;

    public static InventoryProvider of(int size, String title) {
        return create()
                .size(size)
                .title(title);
    }

    public static InventoryProvider create() {
        return new InventoryProvider();
    }

    @SuppressWarnings("all")
    public static InventoryProvider of(YamlConfiguration config, String path, OfflinePlayer source) {
        InventoryProvider inv = create();

        int size = config.getInt(path + ".size");
        String title = config.getString(path + ".title");

        inv.size(size).title(title).source(source);

        for(String id : config.getConfigurationSection(path + ".items").getKeys(false)) {
            int slot = config.getInt(path + ".items." + id + ".slot");
            ItemSerializer<?> item = ItemCreator.of(config, path + ".items." + id);

            inv.setItem(slot, item);
        }

        return inv;
    }

    public static InventoryProvider of(YamlConfiguration config, String path) {
        return of(config, path, null);
    }

    public static InventoryProvider of(FileConfiguration config, String path, OfflinePlayer source) {
        return of((YamlConfiguration) config, path, source);
    }

    public static InventoryProvider of(FileConfiguration config, String path) {
        return of(config, path, null);
    }

    public Inventory inventory() {
        RawInventory rawInventory = inventoryProvider.inventory();

        String title = rawInventory.title();
        int size = rawInventory.size();
        OfflinePlayer source = rawInventory.source();
        InventoryHolder holder = rawInventory.holder();
        Map<Integer, ItemSerializer<?>> content = rawInventory.content();

        Component finalTitle;
        boolean isSourceNull = source == null;
        if(!isSourceNull) {
            finalTitle = StringFormatter.colorWithPlaceholders(title, source);
        } else {
            finalTitle = StringFormatter.color(title);
        }

        Inventory inv = Bukkit.createInventory(holder, size, finalTitle);
        for(Map.Entry<Integer, ItemSerializer<?>> entry : content.entrySet()) {
            int slot = entry.getKey();
            ItemSerializer<?> item = entry.getValue();

            if(slot >= size) continue;
            if(!isSourceNull) item.source(source);

            inv.setItem(slot, item.build().itemStack());
        }

        return inv;
    }

    @Override
    public InventoryCreator clone() {
        try {
            InventoryCreator clone = (InventoryCreator) super.clone();
            clone.inventoryProvider = this.inventoryProvider.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
