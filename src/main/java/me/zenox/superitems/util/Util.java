package me.zenox.superitems.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
}
