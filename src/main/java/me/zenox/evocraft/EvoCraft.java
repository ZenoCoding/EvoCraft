package me.zenox.evocraft;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.AuraSkillsBukkit;
import me.zenox.evocraft.abilities.AbilityRegistry;
import me.zenox.evocraft.attribute.AttributeRegistry;
import me.zenox.evocraft.command.Command;
import me.zenox.evocraft.data.ConfigLoader;
import me.zenox.evocraft.data.LanguageLoader;
import me.zenox.evocraft.data.PlayerDataManager;
import me.zenox.evocraft.enchant.EnchantRegistry;
import me.zenox.evocraft.events.*;
import me.zenox.evocraft.gameclass.ClassAbilityListener;
import me.zenox.evocraft.item.ItemRegistry;
import me.zenox.evocraft.item.VanillaItem;
import me.zenox.evocraft.network.GlowFilter;
import me.zenox.evocraft.recipe.RecipeRegistry;
import me.zenox.evocraft.util.Util;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public final class EvoCraft extends JavaPlugin {

    private static EvoCraft plugin;

    private static Economy econ = null;
    private static Permission perms = null;

    public boolean isUsingWorldGuard;
    private static LanguageLoader languageLoader;
    private static ConfigLoader configLoader;
    private static ProtocolManager protocolManager;
    private static PlayerDataManager playerDataManager;
    private static AuraSkillsApi auraSkillsApi;
    private static AuraSkillsBukkit auraSkillsBukkit;

    public static EvoCraft getPlugin() {
        return plugin;
    }


    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        plugin = this;
        plugin.getLogger().info("EvoCraft v" + plugin.getDescription().getVersion() + " loaded.");

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

        if (!setupPermissions() ) {
            Util.logToConsole("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        auraSkillsApi = AuraSkillsApi.get();
        auraSkillsBukkit = AuraSkillsBukkit.get();

        playerDataManager = new PlayerDataManager();

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

        Util.logToConsole("v" + plugin.getDescription().getVersion() + " enabled in &e" + (System.currentTimeMillis() - startTime) + "&fms.");
    }

    private void registerListeners() {
        new PlayerUseItemEvent(plugin);
        new OtherEvent(plugin);
        new InventoryListener(plugin);
        new DimensionLocker(plugin);
        new DeathManager(plugin);
        new ClassAbilityListener(plugin);
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

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }


    public static void registerGlobalGUIItems(){
        // Menu Glass Item
        Structure.addGlobalIngredient('#', new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setDisplayName(""));
    }


    public static LanguageLoader getLang() {
        return languageLoader;
    }

    public static ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public void reload() {
        this.reloadConfig();
        this.languageLoader = new LanguageLoader(this);
    }

    @Override
    public void onDisable() {
        long startTime = System.currentTimeMillis();
        playerDataManager.shutdown();
        Util.logToConsole("v" + plugin.getDescription().getVersion() + " disabled in &e" + (System.currentTimeMillis() - startTime) + "&fms.");
    }
}
