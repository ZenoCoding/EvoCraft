package me.zenox.superitems;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.zenox.superitems.command.MainCommand;
import me.zenox.superitems.events.OtherEvent;
import me.zenox.superitems.events.PlayerUseItemEvent;
import me.zenox.superitems.items.ItemRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperItems extends JavaPlugin {

    private static SuperItems plugin;

    public boolean isUsingWorldGuard;

    public static SuperItems getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getLogger().info("SuperItems v" + plugin.getDescription().getVersion() + " loaded.");

        //ItemRegistry.registerItems();
        ItemRegistry.registerRecipes();

        // Dependency check
        try {
            WorldGuard.getInstance();
            WorldGuardPlugin.inst();
            this.isUsingWorldGuard = true;

        } catch (NoClassDefFoundError e) {
            this.isUsingWorldGuard = false;
        }

        new PlayerUseItemEvent(plugin);
        new OtherEvent(plugin);
        new MainCommand(plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
