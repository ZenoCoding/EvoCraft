package me.zenox.superitems.items;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.stats.Stat;
import me.zenox.superitems.SuperItems;
import me.zenox.superitems.items.abilities.ItemAbility;
import me.zenox.superitems.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class SuperItem extends BasicItem {
    public static NamespacedKey GLOBAL_ID = new NamespacedKey(SuperItems.getPlugin(), "superitem");
    private final List<ItemAbility> abilities;

    public SuperItem(String name, String id, Rarity rarity, Type type, Material material, boolean hasRecipe, int recipeAmount, Map<Stat, Double> stats, List<ItemAbility> abilities) {
        super(name, id, rarity, type, material, stats);
        this.abilities = abilities;
    }

//
//    @Override
//    public ItemStack getItemStack(Integer amount) {
//        return getItemStackWithData(amount, new ArrayList<>());
//    }
//
//    @Override
//    public ItemStack getItemStackWithData(Integer amount, List<String> data) {
//        ItemStack item = new ItemStack(this.getMaterial());
//        item.setItemMeta(this.getMeta());
//        item.setAmount(amount);
//        ItemMeta meta = item.getItemMeta();
//        // Add persistent data
//        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
//        dataContainer.set(GLOBAL_ID, PersistentDataType.STRING, this.getId());
//
//        List<String> lore = new ArrayList<>();
//        boolean hasStats = false;
//        for(Map.Entry<Stat, Double> entry : this.getStats().entrySet()){
//            item = AureliumAPI.addItemModifier(item, entry.getKey(), entry.getValue(), true);
//            hasStats = true;
//        }
//
//        if(meta.getLore() != null) lore.addAll(item.getItemMeta().getLore());
//        if(hasStats) lore.add("");
//
//        String loreconcat = "";
//
//        for (String loreitem : lore) {
//            loreconcat = loreconcat + loreitem + "∫";
//        }
//
//        loreconcat.formatted(data);
//        List<String> loreformatted = new ArrayList();
//        loreformatted.addAll(Arrays.asList(loreconcat.split("∫")));
//
//        loreformatted.add("");
//
//        loreformatted.add(this.getRarity().color() + this.getRarity().getName() + " " + this.getType().getName());
//        meta.setLore(loreformatted);
//        meta.setDisplayName(this.getRarity().color() + this.getName());
//        item.setItemMeta(meta);
//        return item;
//
//    }

    @Override
    protected void writeAbilityLore(List<String> loreformatted){
        for (ItemAbility ability : this.abilities) {
            loreformatted.add("");
            loreformatted.add(ChatColor.GOLD + "Ability: " + ability.getName() + ChatColor.YELLOW + ChatColor.BOLD + " " + ability.getAction().getName());
            loreformatted.addAll(ability.getLore());

            if (ability.getManaCost() > 0) {
                loreformatted.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            }
            if (ability.getCooldown() > 0) {
                loreformatted.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + ability.getCooldown() + "s");
            }
        }
    }

    public List<ItemAbility> getAbilities() {
        return this.abilities;
    }


}

