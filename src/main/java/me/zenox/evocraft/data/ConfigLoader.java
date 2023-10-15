package me.zenox.evocraft.data;

import me.zenox.evocraft.EvoCraft;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader {
    private EvoCraft plugin;
    private final FileConfiguration config;

    public ConfigLoader(EvoCraft plugin) {
        config = plugin.getConfig();
        if (config.getBoolean("force_update_default")) plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
