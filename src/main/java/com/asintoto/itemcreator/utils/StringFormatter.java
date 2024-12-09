package com.asintoto.itemcreator.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class StringFormatter {
    public static Component color(String msg) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(msg).decoration(TextDecoration.ITALIC, false);
    }

    public static Component colorWithPlaceholders(String msg, OfflinePlayer player) {
        String message = msg;
        if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        return color(message);
    }
}
