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
import me.zenox.superitems.command.MainCommand;
import me.zenox.superitems.data.ConfigLoader;
import me.zenox.superitems.data.LanguageLoader;
import me.zenox.superitems.enchant.EnchantRegistry;
import me.zenox.superitems.events.DimensionLocker;
import me.zenox.superitems.events.InventoryListener;
import me.zenox.superitems.events.OtherEvent;
import me.zenox.superitems.events.PlayerUseItemEvent;
import me.zenox.superitems.item.ItemRegistry;
import me.zenox.superitems.item.VanillaItem;
import me.zenox.superitems.network.GlowFilter;
import me.zenox.superitems.recipe.RecipeRegistry;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class SuperItems extends JavaPlugin {

    private static SuperItems plugin;
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

        modifiers = new Modifiers(AureliumSkills.getPlugin(AureliumSkills.class));

        configLoader = new ConfigLoader(plugin);
        languageLoader = new LanguageLoader(plugin);

        registerGlobalGUIItems();

        // Item Packet/Network filters
        new GlowFilter(this, protocolManager);

        AttributeRegistry.registerAttributes();
        AbilityRegistry.registerAbilities();
        ItemRegistry.registerRecipes();
        ItemRegistry.registerItems();
        VanillaItem.registerItems();
        RecipeRegistry.registerRecipes();
        EnchantRegistry.registerEnchants();

        new MainCommand(plugin);

        registerListeners();

    }

    private void registerListeners() {
        new PlayerUseItemEvent(plugin);
        new OtherEvent(plugin);
        new InventoryListener(plugin);
        new DimensionLocker(plugin);
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

    public void reload() {
        this.reloadConfig();
        this.languageLoader = new LanguageLoader(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
