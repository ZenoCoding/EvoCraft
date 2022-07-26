package me.zenox.superitems.items;

import me.zenox.superitems.SuperItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SuperItem extends BasicItem{
    public static NamespacedKey GLOBAL_ID = new NamespacedKey(SuperItems.getPlugin(), "superitem");
    private List<ItemAbility> abilities;

    public SuperItem(String name, String id, Rarity rarity, Type type, List<ItemAbility> abilities, Material material, ItemMeta metadata){
        super(name, id, rarity, type, material, metadata);
        this.abilities = abilities;
    }

    @Override
    public ItemStack getItemStack(Integer amount){
        ItemStack item = new ItemStack(this.getMaterial());
        item.setItemMeta(this.getMeta());
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        // Add persistent data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(GLOBAL_ID, PersistentDataType.STRING, this.getId());

        List<String> lore = (meta.getLore() == null) ? new ArrayList() : meta.getLore();
        for (ItemAbility ability: this.abilities) {
            lore.add("");
            lore.add(ChatColor.GOLD + "Ability: " + ability.getName() + ChatColor.YELLOW + ChatColor.BOLD + " " + ability.getAction().getName());
            lore.addAll(ability.getLore());

            if (ability.getManaCost() > 0) {
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            }
            if (ability.getCooldown() > 0) {
                lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getCooldown() + "s");
            }
        }
        lore.add("");
        lore.add(this.getRarity().color() + this.getRarity().getName() + " " + this.getType().getName());
        meta.setLore(lore);
        meta.setDisplayName(this.getRarity().color() + this.getName());
        item.setItemMeta(meta);
        return item;

    }

    public List<ItemAbility> getAbilities() {
        return this.abilities;
    }


}
