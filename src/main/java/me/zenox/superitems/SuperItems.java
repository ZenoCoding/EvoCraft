package me.zenox.superitems;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.modifier.Modifiers;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.zenox.superitems.command.MainCommand;
import me.zenox.superitems.events.InventoryListener;
import me.zenox.superitems.events.OtherEvent;
import me.zenox.superitems.events.PlayerUseItemEvent;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.lang.LanguageLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperItems extends JavaPlugin {

    private static SuperItems plugin;

    public boolean isUsingWorldGuard;

    public Modifiers modifiers;

    public static SuperItems getPlugin() {
        return plugin;
    }


    @Override
    public void onEnable() {
        plugin = this;
        plugin.getLogger().info("SuperItems v" + plugin.getDescription().getVersion() + " loaded.");

        modifiers = new Modifiers(AureliumSkills.getPlugin(AureliumSkills.class));

        //new LanguageLoader(plugin);

        ItemRegistry.registerRecipes();

        // Dependency check
        try {
            WorldGuard.getInstance();
            WorldGuardPlugin.inst();
            isUsingWorldGuard = true;

        } catch (NoClassDefFoundError e) {
            isUsingWorldGuard = false;
        }

        new MainCommand(plugin);

        registerListeners();

    }

    private void registerListeners(){
        new PlayerUseItemEvent(plugin);
        new OtherEvent(plugin);
        new InventoryListener(plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
