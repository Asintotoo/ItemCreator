package com.asintoto.itemcreator.items.serializers;

import com.asintoto.itemcreator.ItemCreator;
import com.asintoto.itemcreator.items.RawItem;
import com.asintoto.itemcreator.items.providers.SkullProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
@Accessors(fluent = true) @SuppressWarnings("unchecked")
public abstract class ItemSerializer<T extends ItemSerializer<T>> implements Cloneable {

    protected RawItem item = new RawItem();

    public T item(final ItemStack item) {
        this.item.itemStack(item);
        return (T) this;
    }

    public T material(final Material material) {
        this.item.material(material);
        return (T) this;
    }

    public T data(final byte data) {
        this.item.data(data);
        return (T) this;
    }

    public T customModelData(final int customModelData) {
        this.item.customModelData(customModelData);
        return (T) this;
    }

    public T amount(final int amount) {
        this.item.amount(amount);
        return (T) this;
    }

    public T display_name(final String display_name) {
        this.item.display_name(display_name);
        return (T) this;
    }

    public T lore(final String... lore) {
        this.item.lore(lore);
        return (T) this;
    }

    public T flags(final ItemFlag... flags) {
        this.item.flags(flags);
        return (T) this;
    }

    public T flags(final String... flags) {
        try {
            final List<ItemFlag> list = new ArrayList<>();
            for (final String flag : flags) list.add(ItemFlag.valueOf(flag));
            this.item.flags(list.toArray(new ItemFlag[0]));
        } catch (IllegalArgumentException ignore) { }
        return (T) this;
    }

    public T enchantment(final Enchantment enchantment, final int level) {
        this.item.enchantments().put(enchantment, level);
        return (T) this;
    }

    public T enchantment(final String enchantment) {
        if (enchantment == null || enchantment.isEmpty()) return (T) this;
        if (!enchantment.contains(":") && enchantment.split(":").length != 2) return (T) this;
        final String arg1 = enchantment.split(":")[0];
        final int level = Integer.parseInt(enchantment.split(":")[1]);
        final Enchantment enchant = Enchantment.getByName(arg1.toUpperCase());
        if (enchant == null) return (T) this;
        this.item.enchantments().put(enchant, level);
        return (T) this;
    }

    public T enchantments(final Map<Enchantment, Integer> enchantments) {
        this.item.enchantments(enchantments);
        return (T) this;
    }

    public T unbreakable(final boolean unbreakable) {
        this.item.unbreakable(unbreakable);
        return (T) this;
    }

    public T glow(final boolean glow) {
        this.item.glow(glow);
        return (T) this;
    }

    public T nbt(final JavaPlugin plugin, final String key, final Object value) {
        this.item.nbt().put(plugin, new AbstractMap.SimpleEntry<>(key, value));
        return (T) this;
    }

    public T nbt(final Map<JavaPlugin, Map.Entry<String, Object>> nbt) {
        this.item.nbt(nbt);
        return (T) this;
    }

    public T source(final OfflinePlayer source) {
        this.item.source(source);
        return (T) this;
    }

    public T copyFrom(final ItemSerializer<?> serializer, final boolean force) {
        if (serializer == null) return (T) this;
        final RawItem item = serializer.item;
        if (!force) {
            if (item.itemStack() != null) this.item(item.itemStack());
            if (item.material() != null) this.material(item.material());
            if (item.data() > 0) this.data(item.data());
            if (item.customModelData() > 0) this.customModelData(item.customModelData());
            if (item.amount() > 0) this.amount(item.amount());
            if (item.display_name() != null) this.display_name(MiniMessage.miniMessage().serialize(item.display_name()));
            if (item.lore() != null) this.lore(Arrays.stream(item.lore()).map(MiniMessage.miniMessage()::serialize).toArray(String[]::new));
            if (item.flags() != null) this.flags(item.flags());
            if (!item.enchantments().isEmpty()) this.enchantments(item.enchantments());
            if (item.glow()) this.glow(true);
            if (item.unbreakable()) this.unbreakable(true);
            if (item.source() != null) this.source(item.source());
            if (!item.nbt().isEmpty()) this.nbt(item.nbt());
            if (this instanceof SkullProvider skull) {
                if (item.owner() != null) skull.owner(item.owner().getName());
                if (item.texture() != null) skull.texture(item.texture());
            }
        } else {
            this.item(item.itemStack())
                    .material(item.material())
                    .data(item.data())
                    .customModelData(item.customModelData())
                    .amount(item.amount())
                    .display_name(MiniMessage.miniMessage().serialize(item.display_name()))
                    .lore(Arrays.stream(item.lore()).map(MiniMessage.miniMessage()::serialize).toArray(String[]::new))
                    .flags(item.flags())
                    .enchantments(item.enchantments())
                    .glow(item.glow())
                    .unbreakable(item.unbreakable())
                    .source(item.source())
                    .nbt(item.nbt());
            if (this instanceof SkullProvider) ((SkullProvider) this).owner(item.owner().getName()).texture(item.texture());
        }
        return (T) this;
    }

    public T copyFrom(final ItemSerializer<?> serializer) {
        return this.copyFrom(serializer, false);
    }

    public <V extends ItemSerializer<V>> V get() {
        return (V) this;
    }

    public ItemCreator build() {
        return new ItemCreator(this);
    }

    public void give(Player player) {
        ItemStack item = build().itemStack();
        player.getInventory().addItem(item);
    }

    @Override
    public ItemSerializer<T> clone() {
        try {
            final ItemSerializer<T> clone = (ItemSerializer<T>) super.clone();
            clone.item = this.item.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
