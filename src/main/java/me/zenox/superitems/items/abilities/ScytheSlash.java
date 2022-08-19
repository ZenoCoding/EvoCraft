package me.zenox.superitems.items.abilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.zenox.superitems.SuperItems;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ScytheSlash extends ItemAbility {



    public ScytheSlash() {
        super("Scythe Slash", "scythe_slash", AbilityAction.SHIFT_LEFT_CLICK, 0, 1.5);

        this.addLineToLore(ChatColor.GRAY + "can you do this part the lore is in the next qoutes" + ChatColor.BLACK + "Swings your scythe around to deal massive AOE near you");
        this.addLineToLore(ChatColor.GRAY + "Deals massive" + ChatColor.RED + " damage" + ChatColor.GRAY + " on impact.");


    }

    @Override
    protected void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Random r = new Random();
        Player p = e.getPlayer();
        World w = p.getWorld();



        //ttyl
    }
}
