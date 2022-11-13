package me.zenox.superitems.abilities;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Transcendence extends ItemAbility {
    public Transcendence() {
        super("dimensional_travel", AbilityAction.RIGHT_CLICK_ALL, 350, 0);

        this.addLineToLore(ChatColor.GRAY + "Crates a " + ChatColor.LIGHT_PURPLE + "rift" + ChatColor.GRAY + " into the next dimension.");
        this.addLineToLore(ChatColor.GRAY + "Cycles through the " + ChatColor.RED + "The Nether, " + ChatColor.GREEN + "The Overworld, " + ChatColor.DARK_GRAY + "The End.");
        this.addLineToLore("");
        this.addLineToLore(ChatColor.AQUA + "Pages: " + ChatColor.LIGHT_PURPLE + "100");
    }

    // TODO: Change this to a CIM Variable

    @Override
    public void runExecutable(Event event, Player p, ItemStack item) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        ItemMeta meta = item.getItemMeta();

        //Remove Pages
        NamespacedKey key = new NamespacedKey(SuperItems.getPlugin(), "dimensional_pages");
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        int pages = 99;

        if (dataContainer.has(key, PersistentDataType.INTEGER)) {
            pages = dataContainer.get(key, PersistentDataType.INTEGER) - 1;
            dataContainer.set(key, PersistentDataType.INTEGER, pages);
        } else {
            dataContainer.set(key, PersistentDataType.INTEGER, 99);
        }

        // Update lore
        List<String> lore = meta.getLore();
        for (String lorestring : lore) {
            if (lorestring.startsWith(ChatColor.AQUA + "Pages: ")) {
                lore.set(lore.indexOf(lorestring), ChatColor.AQUA + "Pages: " + ChatColor.LIGHT_PURPLE + pages);
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        if (pages == 0) {
            item.setAmount(item.getAmount() - 1);
            Util.sendMessage(p, ChatColor.WHITE + "Your " + ChatColor.DARK_PURPLE + "[Dimensional Journal]" + ChatColor.WHITE + " ran out of pages and exploded!");
        }

        List<World> worldList = List.of(p.getServer().getWorld("world_nether"), p.getServer().getWorld("world"), p.getServer().getWorld("world_the_end"));

        if (worldList.contains(p.getWorld())) {
            int index = worldList.indexOf(p.getWorld());
            double pitch = p.getLocation().getPitch();
            if (pitch >= 0) {
                if (worldList.size() - 1 == index) p.teleport(worldList.get(0).getSpawnLocation());
                else p.teleport(worldList.get(index + 1).getSpawnLocation());
            } else {
                if (index == 0) {
                    p.teleport(worldList.get(worldList.size() - 1).getSpawnLocation());
                } else p.teleport(worldList.get(index - 1).getSpawnLocation());
            }
        } else {
            p.teleport(worldList.get(1).getSpawnLocation());
        }

        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 0f);
        p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 1.2f);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);


    }
}
