package com.asintoto.itemcreator.hooks;

import com.asintoto.itemcreator.utils.PluginChecker;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPIHook {
    private static boolean checkPlaceholderAPI() {
        return PluginChecker.pluginExists("PlaceholderAPI");
    }

    public static String setPlaceholders(String msg, OfflinePlayer player) {
        if(!checkPlaceholderAPI()) {
            return msg;
        }

        return PlaceholderAPI.setPlaceholders(player, msg);
    }

    public static String setPlaceholders(String msg) {
        return setPlaceholders(msg, null);
    }
}
