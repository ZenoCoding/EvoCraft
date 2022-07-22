package me.zenox.superitems;

import me.zenox.superitems.command.MainCommand;
import me.zenox.superitems.events.PlayerUseItemEvent;
import me.zenox.superitems.items.SuperItemRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperItems extends JavaPlugin {

    private static SuperItems plugin;

    public static SuperItemRegistry registry;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getLogger().info("SuperItems v" + plugin.getDescription().getVersion() + " loaded.");
        registry = new SuperItemRegistry(plugin);
        new PlayerUseItemEvent(plugin);
        new MainCommand(plugin);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SuperItems getPlugin(){
        return plugin;
    }
}
