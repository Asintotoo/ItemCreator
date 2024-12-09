package com.asintoto.itemcreator;

import com.asintoto.itemcreator.items.RawItem;
import com.asintoto.itemcreator.items.providers.ItemProvider;
import com.asintoto.itemcreator.items.providers.SkullProvider;
import com.asintoto.itemcreator.items.serializers.ItemSerializer;
import com.asintoto.itemcreator.utils.ItemsAdderHook;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class ItemCreator implements Cloneable{
    private ItemSerializer<?> itemSerializer;

    public ItemSerializer<?> serialized() {
        return this.itemSerializer;
    }

    public RawItem deserialized() {
        return this.serialized().item();
    }

    public ItemStack itemStack() {
        final RawItem serializer = this.deserialized();
        if (serializer == null) throw new IllegalArgumentException("Serializer cannot be null");
        final Optional<ItemStack> itemStack = Optional.ofNullable(serializer.itemStack());
        final Optional<Material> material = Optional.ofNullable(serializer.material());
        final Optional<String> texture = Optional.ofNullable(serializer.texture());
        final Optional<OfflinePlayer> owner = Optional.ofNullable(serializer.owner());
        final Optional<Component> name = Optional.ofNullable(serializer.display_name());
        final Optional<Integer> amount = Optional.of(serializer.amount());
        final Optional<Component[]> lore = Optional.of(serializer.lore());
        final Optional<Integer> customModelData = Optional.of(serializer.customModelData());
        final Optional<ItemFlag[]> flags = Optional.ofNullable(serializer.flags());
        final Optional<Map<Enchantment, Integer>> enchantments = Optional.ofNullable(serializer.enchantments());
        final Optional<Boolean> glow = Optional.of(serializer.glow());
        final Optional<Boolean> unbreakable = Optional.of(serializer.unbreakable());
        final Optional<Map<JavaPlugin, Map.Entry<String, Object>>> nbt = Optional.ofNullable(serializer.nbt());

        final ItemStack item = itemStack.orElseGet(() -> new ItemStack(material.orElse(Material.STONE)));
        amount.ifPresentOrElse(a -> item.setAmount(a < 1 ? 1 : a), () -> item.setAmount(1));
        item.editMeta(meta -> {
            name.ifPresent(meta::displayName);
            lore.ifPresent(l -> meta.lore(List.of(l)));
            meta.setCustomModelData(customModelData.get());
            flags.ifPresent(meta::addItemFlags);
            enchantments.ifPresent(e -> e.forEach((enchant, level) -> meta.addEnchant(enchant, level, true)));
            if (glow.get()) meta.setEnchantmentGlintOverride(true);
            if (unbreakable.get()) meta.setUnbreakable(true);
            nbt.ifPresent(map -> map.forEach((plugin, entry) -> {
                final NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
                final Object value = entry.getValue();
                if (value instanceof String) meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, (String) value);
                if (value instanceof Integer) meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, (Integer) value);
                if (value instanceof Double) meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, (Double) value);
                if (value instanceof Float) meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, (Float) value);
                if (value instanceof Long) meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, (Long) value);
                if (value instanceof Byte) meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (Byte) value);
                if (value instanceof Short) meta.getPersistentDataContainer().set(key, PersistentDataType.SHORT, (Short) value);
                if (value instanceof Boolean) meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) ((boolean) value ? 1 : 0));
            }));
        });

        if (item.getType() == Material.PLAYER_HEAD) {
            final PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
            texture.ifPresent(t -> playerProfile.setProperty(new ProfileProperty("textures", t)));
            item.editMeta(SkullMeta.class, meta -> {
                if (texture.isPresent()) meta.setPlayerProfile(playerProfile);
                owner.ifPresent(meta::setOwningPlayer);
            });
        }
        return item;
    }

    public static ItemProvider of(final Material material) {
        return new ItemProvider(material);
    }

    public static ItemProvider of(final ItemStack item) {
        return new ItemProvider(item);
    }

    public static SkullProvider ofSkull() {
        return new SkullProvider();
    }

    public static ItemProvider ofItemsAdder(String namespace_id) {
        return new ItemProvider(ItemsAdderHook.getItemsAdderItemStack(namespace_id));
    }

    @SuppressWarnings("unchecked")
    public static <T extends ItemSerializer<T>> ItemSerializer<T> of(final YamlConfiguration config, final String path) {
        final Optional<String> itemsadderID = Optional.ofNullable(config.getString(path + ".itemsadder"));
        if(itemsadderID.isPresent()) {
            return (T) new ItemProvider(ItemsAdderHook.getItemsAdderItemStack(itemsadderID.get()));
        }

        final String material = config.getString(path + ".material");
        final String texture = config.getString(path + ".texture");
        final String owner = config.getString(path + ".owner");
        final String name = config.getString(path + ".display-name");
        final int amount = config.getInt(path + ".amount");
        final List<String> lore = config.getStringList(path + ".lore");
        final int customModelData = config.getInt(path + ".custom-model-data");
        final boolean glow = config.getBoolean(path + ".glow");
        final List<String> enchantments = config.getStringList(path + ".enchantments");

        T provider;
        try {
            final Material materialType = Material.valueOf(material);
            if (materialType == Material.PLAYER_HEAD) {
                provider = (T) new SkullProvider();
                final SkullProvider skull = (SkullProvider) provider;
                skull.owner(owner);
                skull.texture(texture);
            } else provider = (T) new ItemProvider(materialType);
        } catch (final NullPointerException e) {
            provider = (T) new ItemProvider(Material.STONE);
        }

        provider.amount(amount);
        provider.customModelData(customModelData);
        provider.display_name(name);
        provider.lore(lore.toArray(new String[0]));
        provider.glow(glow);
        if (!enchantments.isEmpty()) enchantments.forEach(provider::enchantment);
        return provider;
    }

    @Override
    public ItemCreator clone() {
        try {
            final ItemCreator clone = (ItemCreator) super.clone();
            clone.itemSerializer = this.itemSerializer.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}