package me.zenox.superitems.item.abilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Geo;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.zenox.superitems.util.Util.getNearbyBlocks;

public class SoulRift extends ItemAbility {
    public SoulRift() {
        super("soul_rift", AbilityAction.RIGHT_CLICK_ALL, 50, 60);

        this.addLineToLore(ChatColor.GRAY + "Deploys a" + ChatColor.AQUA + " soul crystal" + ChatColor.GRAY + ", which lasts for" + ChatColor.GREEN + " 5s" + ChatColor.GRAY + " and");
        this.addLineToLore(ChatColor.GRAY + "pulls entities in, dealing " + ChatColor.RED + "true damage" + ChatColor.GRAY + " every second.");
    }
}
