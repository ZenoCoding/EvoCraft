package me.zenox.superitems.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void sendMessage(Player p, String message){
        sendMessage(p, message, true);
    }

    public static void sendMessage(Player p, String message, boolean prefix){
        p.sendMessage(prefix ? ChatColor.GOLD + "[SuperItems] " + message : message);
    }


    public static void logToConsole(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SuperItems] " + message);
    }

    public static void broadcast(String message){
        broadcast(message, true);
    }

    public static void broadcast(String message, Boolean prefix){
        Bukkit.broadcastMessage(prefix ? ChatColor.GOLD + "[SuperItems] " + message : message);
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
