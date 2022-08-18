//package me.zenox.superitems.items.abilities;
//
//import me.zenox.superitems.SuperItems;
//import me.zenox.superitems.util.Util;
//import org.bukkit.*;
//import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.persistence.PersistentDataContainer;
//import org.bukkit.persistence.PersistentDataType;
//import org.bukkit.util.Vector;
//
//import java.util.List;
//
//public class VoidWarp extends ItemAbility {
//    public VoidWarp() {
//        super("Void Warp", "void_warp", AbilityAction.RIGHT_CLICK_ALL, 50, 0);
//
//        this.addLineToLore(ChatColor.DARK_PURPLE + "Not Copied From Hypixel Skyblock... also teleports you 5 blocks in the direction your facing");
//
//    }
//
//    @Override
//    public void runExecutable(PlayerEvent event) {
//        PlayerInteractEvent e = ((PlayerInteractEvent) event);
//        Player p = e.getPlayer();
//        ItemStack item = e.getItem();
//        ItemMeta meta = item.getItemMeta();
//
//        Vector dir = p.getLocation().getDirection();
//        Location loc = p.getLocation();
//        Location hitbloc = p.rayTraceBlocks(5, FluidCollisionMode.NEVER).getHitBlock();
//        Location endloc = p.rayTraceBlocks(5, FluidCollisionMode.NEVER).getHitBlock().getLocation();
//        // check if block is infront of player
//        if (loc.distanceSquared(endloc) <= 25) {
//            p.teleport(endloc);
//        } else {
//            p.teleport();
//        }
////gtg soon
//    }
//}
