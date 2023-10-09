package me.zenox.superitems;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.modifier.Modifiers;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.item.builder.ItemBuilder;
import me.zenox.superitems.abilities.AbilityRegistry;
import me.zenox.superitems.attribute.AttributeRegistry;
import me.zenox.superitems.command.Command;
import me.zenox.superitems.data.ConfigLoader;
import me.zenox.superitems.data.LanguageLoader;
import me.zenox.superitems.enchant.EnchantRegistry;
import me.zenox.superitems.events.*;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.item.VanillaItem;
import me.zenox.superitems.network.GlowFilter;
import me.zenox.superitems.recipe.RecipeRegistry;
import me.zenox.superitems.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperItems extends JavaPlugin {

    private static SuperItems plugin;

    private static Economy econ = null;

    public boolean isUsingWorldGuard;
    public Modifiers modifiers;
    private LanguageLoader languageLoader;
    private ConfigLoader configLoader;
    private ProtocolManager protocolManager;

    public static SuperItems getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getLogger().info("SuperItems v" + plugin.getDescription().getVersion() + " loaded.");

        // Dependencies
        protocolManager = ProtocolLibrary.getProtocolManager();

        // Dependency check
        try {
            WorldGuard.getInstance();
            WorldGuardPlugin.inst();
            isUsingWorldGuard = true;

        } catch (NoClassDefFoundError e) {
            isUsingWorldGuard = false;
        }

        if (!setupEconomy() ) {
            Util.logToConsole("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        modifiers = new Modifiers(AureliumSkills.getPlugin(AureliumSkills.class));

        configLoader = new ConfigLoader(plugin);
        languageLoader = new LanguageLoader(plugin);

        registerGlobalGUIItems();

        // Item Packet/Network filters
        new GlowFilter(this, protocolManager);

        AttributeRegistry.registerAttributes();
        AbilityRegistry.registerAbilities();
        VanillaItem.registerItems();
        ItemRegistry.registerRecipes();
        ItemRegistry.registerItems();
        RecipeRegistry.registerRecipes();
        EnchantRegistry.registerEnchants();

        new Command(plugin);

        registerListeners();

    }

    private void registerListeners() {
        new PlayerUseItemEvent(plugin);
        new OtherEvent(plugin);
        new InventoryListener(plugin);
        new DimensionLocker(plugin);
        new DeathManager(plugin);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }


    public static void registerGlobalGUIItems(){
        // Menu Glass Item
        Structure.addGlobalIngredient('#', new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setDisplayName(""));
    }


    public LanguageLoader getLang() {
        return this.languageLoader;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public void reload() {
        this.reloadConfig();
        this.languageLoader = new LanguageLoader(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
