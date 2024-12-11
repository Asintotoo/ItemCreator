package com.asintoto.itemcreator.utils;

import org.bukkit.Bukkit;

public class PluginChecker {
    public static boolean pluginExists(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
    }
}
