package me.zenox.superitems.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Util {


    public static void sendMessage(Player p, String message) {
        sendMessage(p, message, true);
    }

    public static void sendMessage(Player p, String message, boolean prefix) {
        p.sendMessage(prefix ? ChatColor.GOLD + "[SuperItems] " + message : message);
    }

    public static void sendMessage(CommandSender p, String message) {
        sendMessage(p, message, true);
    }

    public static void sendMessage(CommandSender p, String message, boolean prefix) {
        p.sendMessage(prefix ? ChatColor.GOLD + "[SuperItems] " + message : message);
    }

    public static void sendActionBar(Player p, String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public static void logToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SuperItems] " + message);
    }

    public static void broadcast(String message) {
        broadcast(message, true);
    }

    public static void broadcast(String message, Boolean prefix) {
        Bukkit.broadcastMessage(prefix ? ChatColor.GOLD + "[SuperItems] " + message : message);
    }

    @NotNull
    public static ItemStack makeSkull(ItemStack item, String base64EncodedString) {
        final ItemStack skull = item;
        if (item.getType() != Material.PLAYER_HEAD) return item;
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;

        Random r = new Random(base64EncodedString.hashCode());

        byte[] array = new byte[16];
        r.nextBytes(array);
        UUID id = UUID.nameUUIDFromBytes(array);

        GameProfile profile = new GameProfile(id, null);
        profile.getProperties().put("textures", new Property("textures", base64EncodedString));
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }

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
}
