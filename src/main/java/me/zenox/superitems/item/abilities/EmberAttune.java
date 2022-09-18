package me.zenox.superitems.item.abilities;

import me.zenox.superitems.SuperItems;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class EmberAttune extends ItemAbility {
    public EmberAttune() {
        super("Attune", "dark_ember_attune", AbilityAction.LEFT_CLICK_ALL, 0, 0);

        this.addLineToLore(ChatColor.GRAY + "Changes the " + ChatColor.AQUA + "attunement" + ChatColor.GRAY + " of this staff,");
        this.addLineToLore(ChatColor.GRAY + "changing the domain it draws power from.");
        this.addLineToLore("");
        this.addLineToLore(ChatColor.GRAY + "Swap between " + ChatColor.GOLD + "Blazeborn" + ChatColor.GRAY + " and " + ChatColor.DARK_GRAY + "Darksoul");
    }

    @Override
    public void runExecutable(PlayerEvent event) {
        PlayerInteractEvent e = ((PlayerInteractEvent) event);
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        String attunement = dataContainer.get(new NamespacedKey(SuperItems.getPlugin(), "ember_attunement"), PersistentDataType.STRING);
        String message = ChatColor.RED + "Unknown Attunement";
        String newAttunement = "attunement_unknown";
        if (attunement.equalsIgnoreCase("blazeborn")) {
            message = ChatColor.DARK_GRAY + "Darksoul";
            newAttunement = "darksoul";
            meta.setCustomModelData(meta.getCustomModelData()+1);
        } else if (attunement.equalsIgnoreCase("darksoul")) {
            message = ChatColor.GOLD + "Blazeborn";
            newAttunement = "blazeborn";
            meta.setCustomModelData(meta.getCustomModelData()-1);
        }

        dataContainer.set(new NamespacedKey(SuperItems.getPlugin(), "ember_attunement"), PersistentDataType.STRING, newAttunement);

        Util.sendActionBar(p, message);
        p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1f);

        // Update lore
        List<String> lore = item.getItemMeta().getLore();
        for (String lorestring : lore) {
            if (lorestring.startsWith(ChatColor.AQUA + "Attunement: ")) {
                lore.set(lore.indexOf(lorestring), ChatColor.AQUA + "Attunement: " + message);
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
