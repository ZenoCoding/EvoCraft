package me.zenox.evocraft.util;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.modifier.ModifierType;
import com.archyx.aureliumskills.modifier.Modifiers;
import com.archyx.aureliumskills.modifier.StatModifier;
import com.archyx.aureliumskills.stats.Stat;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.zenox.evocraft.EvoCraft;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Util {


    public static void sendMessage(Player p, String message) {
        sendMessage(p, ChatColor.translateAlternateColorCodes('&', message), true);
    }

    public static void sendMessage(@NotNull Player p, String message, boolean prefix) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix ? ChatColor.AQUA + "[Evo" + ChatColor.GREEN + "Craft] " + ChatColor.WHITE + message : ChatColor.WHITE + message));
    }

    public static void sendMessage(CommandSender p, String message) {
        sendMessage(p, ChatColor.translateAlternateColorCodes('&', message), true);
    }

    public static void sendMessage(@NotNull CommandSender p, String message, boolean prefix) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix ? ChatColor.AQUA + "[Evo" + ChatColor.GREEN + "Craft] " + ChatColor.WHITE + message : ChatColor.WHITE + message));
    }

    public static void sendActionBar(@NotNull Player p, String message) {
        EvoCraft.getActionBar().sendAbilityActionBar(p, ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendTitle(@NotNull Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        p.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut);
    }

    public static void logToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Evo" + ChatColor.GREEN + "Craft] " + ChatColor.WHITE + message);
    }

    public static void broadcast(String message) {
        broadcast(ChatColor.translateAlternateColorCodes('&', message), true);
    }

    public static void broadcast(String message, Boolean prefix) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix ? ChatColor.AQUA + "[Evo" + ChatColor.GREEN + "Craft] " + ChatColor.WHITE + message : ChatColor.WHITE + message));
    }

    @NotNull
    public static ItemStack makeSkull(@NotNull ItemStack item, String base64EncodedString) {
        if (item.getType() != Material.PLAYER_HEAD) return item;
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;

        Random r = new Random(base64EncodedString.hashCode());

        byte[] array = new byte[16];
        r.nextBytes(array);
        UUID id = UUID.nameUUIDFromBytes(array);

        PlayerProfile profile = Bukkit.createProfile(id, null);
        profile.getProperties().add(new ProfileProperty("textures", base64EncodedString));
        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates an uuid that is reproducible via the string - similar to hashcode, but in UUID form.
     * @param str The String to pass in
     * @return the uuid
     */
    public static UUID constantUUID(String str){

        Random r = new Random(str.hashCode());

        byte[] array = new byte[16];
        r.nextBytes(array);
        return UUID.nameUUIDFromBytes(array);
    }

    /**
     * Gets nearby blocks given a radius and location
     * @param loc The location
     * @param radius The radius in the orthogonal horizontal directions
     * @param yradius The radius in the y direction
     * @return the list of blocks
     */
    public static List<Block> getNearbyBlocks(Location loc, int radius, int yradius) {
        List<Block> nearbyBlocks = new ArrayList();
        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
            for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                for (int y = loc.getBlockY() + yradius; y >= loc.getBlockY() - yradius; y--) {
                    nearbyBlocks.add(loc.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return nearbyBlocks;
    }

    /**
     * Utility method to check if an entity is invulnerable to damage- should be used to do things like check if an entity should have custom knockback/damage applied to it.
     * @param entity the entity to check
     * @return whether the entity is invulnerable
     */
    public static boolean isInvulnerable(Entity entity){
        return (entity.hasMetadata("NPC") || (entity instanceof Player player && player.getGameMode() == GameMode.CREATIVE));
    }

    /**
     * Gets all the modifiers an item has
     * @param item the item to check
     * @param type the type of modifier to check
     * @return the list of modifiers
     */
    public static List<StatModifier> getAureliumModifiers(ItemStack item, ModifierType type){
        Modifiers modifiers = new Modifiers(AureliumSkills.getPlugin(AureliumSkills.class));
        return modifiers.getModifiers(type, item);
    }

    /**
     * Removes all modifiers of a certain type from an item
     * @param item the item to remove the modifiers from
     * @param type the type of modifiers to remove
     * @param stat the stat to remove the modifiers from
     * @return the item with the modifiers removed
     */
    public static ItemStack removeAureliumModifier(ItemStack item, ModifierType type, Stat stat){
        Modifiers modifiers = new Modifiers(AureliumSkills.getPlugin(AureliumSkills.class));
        return modifiers.removeModifier(type, item, stat);
    }

    /**
     * Rounds a given double to the specified number of decimal places.
     * @param value the value to round
     * @param digits the number of decimal places to round to
     * @return the rounded value
     */
    public static double round(double value, int digits) {
        double factor = Math.pow(10, digits);
        return Math.round(value * factor) / factor;
    }
}
