package me.zenox.superitems.data;

import me.zenox.superitems.SuperItems;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader {
    private SuperItems plugin;
    private final FileConfiguration config;

    public ConfigLoader(SuperItems plugin) {
        config = plugin.getConfig();
        if (config.getBoolean("force_update_default")) plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
