package com.asintoto.itemcreator.items;

import com.asintoto.itemcreator.utils.StringFormatter;
import lombok.Data;
import lombok.experimental.Accessors;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class RawItem {
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Map<JavaPlugin, Map.Entry<String, Object>> nbt = new HashMap<>();
    private OfflinePlayer source;
    private ItemStack itemStack;
    private Material material;
    private byte data = 0;
    private int amount = 1, customModelData = 0;
    private String display_name;
    private String[] lore;
    private ItemFlag[] flags;
    private boolean unbreakable = false, glow = false;
    private String texture;
    private String owner;

    public Component display_name() {
        if (this.display_name == null) return null;
        if (this.source != null) return StringFormatter.colorWithPlaceholders(this.display_name, source);
        return StringFormatter.color(this.display_name);
    }

    public Component[] lore() {
        if (this.lore == null) return null;
        if (this.source != null) return Arrays.stream(this.lore).map(s -> StringFormatter.colorWithPlaceholders(s, source)).toArray(Component[]::new);
        return Arrays.stream(this.lore).map(StringFormatter::color).toArray(Component[]::new);
    }

    public OfflinePlayer owner() {
        if (this.owner == null) return null;
        if (this.source != null) return Bukkit.getOfflinePlayer(PlaceholderAPI.setPlaceholders(this.source, this.owner));
        return Bukkit.getOfflinePlayer(this.owner);
    }

    @Override
    public RawItem clone() {
        try {
            return (RawItem) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
