package me.zenox.superitems;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.zenox.superitems.command.MainCommand;
import me.zenox.superitems.events.OtherEvent;
import me.zenox.superitems.events.PlayerUseItemEvent;
import me.zenox.superitems.items.SuperItemRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperItems extends JavaPlugin {

    private static SuperItems plugin;

    public SuperItemRegistry registry;
    public boolean isUsingWorldGuard;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getLogger().info("SuperItems v" + plugin.getDescription().getVersion() + " loaded.");

        // Dependency check
        try{
            WorldGuard.getInstance();
            WorldGuardPlugin.inst();
            this.isUsingWorldGuard = true;

        } catch (NoClassDefFoundError e){
            this.isUsingWorldGuard = false;
        }

        registry = new SuperItemRegistry(plugin);

        new PlayerUseItemEvent(plugin);
        new OtherEvent(plugin);
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
